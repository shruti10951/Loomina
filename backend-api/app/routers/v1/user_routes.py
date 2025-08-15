from fastapi import APIRouter, HTTPException, Query, Depends
from datetime import timedelta

from app.models.user import User

from app.schemas.user import (
    CreateUserSchema,
    UpdateUserSchema,
    UserResponseSchema,
    MinimalUserSchema,
)
from app.schemas.auth import TokenResponse

from app.core.object_id import PyObjectId
from app.core.config import settings

from app.utils.hashing import hash_password
from app.utils.jwt import create_access_token
from app.utils.security import get_current_user

from typing import List

from beanie.operators import In, Push, Pull

router = APIRouter(prefix="/users", tags=["Users"])

@router.post("/", response_model=TokenResponse, status_code=201)
async def register_user(user_data: CreateUserSchema):
    # Normalize inputs
    user_data.username = user_data.username.strip()
    user_data.email = user_data.email.strip().lower()

    # Validate lengths
    if len(user_data.username) > 30:
        raise HTTPException(status_code=400, detail="Username too long (max 30 chars)")
    if len(user_data.password) < 8:
        raise HTTPException(status_code=400, detail="Password must be at least 8 characters")

    # Check for duplicates
    if await User.find_one(User.username == user_data.username):
        raise HTTPException(status_code=409, detail="Username already taken")
    if await User.find_one(User.email == user_data.email):
        raise HTTPException(status_code=409, detail="Email already registered")

    # Hash password
    hashed_pwd = hash_password(user_data.password)

    # Default profile image
    profile_image = user_data.userProfileImage or "https://images.alphacoders.com/135/1350043.png"

    # Create user
    user = User(
        username=user_data.username,
        email=user_data.email,
        password=hashed_pwd,
        bio=user_data.bio,
        userProfileImage=profile_image,
        favouriteGenres=user_data.favouriteGenres,
        favouriteTags=user_data.favouriteTags,
    )
    await user.insert()

    # Automatically issue JWT
    access_token_expires = timedelta(minutes=settings.ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": user.email},
        expires_delta=access_token_expires
    )

    return {"access_token": access_token, "token_type": "bearer"}


@router.get("/me", response_model=UserResponseSchema)
async def get_me(current_user: User = Depends(get_current_user)):
    """
    Returns the currently authenticated user's profile.
    Requires a valid Bearer token.
    """
    return current_user


# Get user by ID
@router.get("/{user_id}", response_model=UserResponseSchema)
async def get_user_by_id(user_id: PyObjectId):
    user = await User.get(user_id)
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    return user


# Update user profile
@router.patch("/{user_id}", response_model=UserResponseSchema)
async def update_user_profile(user_id: PyObjectId, updates: UpdateUserSchema):
    user = await User.get(user_id)
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    update_data = updates.model_dump(exclude_unset=True)

    if "username" in update_data:
        update_data["username"] = update_data["username"].strip()
        if len(update_data["username"]) > 30:
            raise HTTPException(status_code=400, detail="Username too long (max 30 chars)")
        existing_user = await User.find_one(User.username == update_data["username"])
        if existing_user and existing_user.id != user.id:
            raise HTTPException(status_code=409, detail="Username already taken")

    for field, value in update_data.items():
        setattr(user, field, value)

    await user.save()
    return user


# Get user by username
@router.get("/username/{username}", response_model=MinimalUserSchema)
async def get_user_by_username(username: str):
    user = await User.find_one(User.username == username.strip())
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    return user


# Get followers (paginated, minimal)
@router.get("/{user_id}/followers", response_model=List[MinimalUserSchema])
async def get_user_followers(
    user_id: PyObjectId,
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
):
    user = await User.get(user_id)
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    
    followers = await User.find(In(User.id, user.followers)).skip(skip).limit(limit).project(MinimalUserSchema).to_list()
    return followers


# Get following (paginated, minimal)
@router.get("/{user_id}/following", response_model=List[MinimalUserSchema])
async def get_user_following(
    user_id: PyObjectId,
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
):
    user = await User.get(user_id)
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    following = await User.find(In(User.id, user.following)).skip(skip).limit(limit).project(MinimalUserSchema).to_list()
    return following


# Follow a user (safe from duplicates)
@router.post("/{user_id}/follow/{target_id}")
async def follow_user(user_id: PyObjectId, target_id: PyObjectId):
    if user_id == target_id:
        raise HTTPException(status_code=400, detail="You cannot follow yourself.")

    user = await User.get(user_id)
    target = await User.get(target_id)

    if not user or not target:
        raise HTTPException(status_code=404, detail="User not found.")

    if target_id in user.following:
        raise HTTPException(status_code=400, detail="Already following this user.")

    # Use $addToSet-like behavior manually
    user.following.append(target_id)
    target.followers.append(user_id)
    await user.save()
    await target.save()

    return {"message": f"{user.username} is now following {target.username}."}


# Unfollow a user
@router.delete("/{user_id}/follow/{target_id}")
async def unfollow_user(user_id: PyObjectId, target_id: PyObjectId):
    if user_id == target_id:
        raise HTTPException(status_code=400, detail="You cannot unfollow yourself.")

    user = await User.get(user_id)
    target = await User.get(target_id)

    if not user or not target:
        raise HTTPException(status_code=404, detail="User not found.")

    if target_id not in user.following:
        raise HTTPException(status_code=400, detail="Not following this user.")

    user.following.remove(target_id)
    target.followers.remove(user_id)
    await user.save()
    await target.save()

    return {"message": f"{user.username} has unfollowed {target.username}."}
