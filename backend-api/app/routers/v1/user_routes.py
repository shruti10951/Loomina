from fastapi import APIRouter, HTTPException, Query, Depends
from datetime import timedelta
from typing import List

from beanie.operators import In

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

# Initialize API Router with prefix and tags for documentation
router = APIRouter(prefix="/users", tags=["Users"])

# ---------------------------
# User Registration
# ---------------------------
@router.post("/", response_model=TokenResponse, status_code=201)
async def register_user(user_data: CreateUserSchema):
    """
    Register a new user.
    - Validates username and password length.
    - Ensures username and email are unique.
    - Hashes password before saving.
    - Assigns a default profile image if none is provided.
    - Returns an access token for immediate login.
    """
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
    profile_image = user_data.userProfileImage or "https://images.alphacoders.com/135/1350043.png"

    # Create and store the user
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

    # Issue JWT for immediate authentication
    access_token_expires = timedelta(minutes=settings.ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": user.email},
        expires_delta=access_token_expires
    )

    return {"access_token": access_token, "token_type": "bearer"}


# ---------------------------
# Current User Profile
# ---------------------------
@router.get("/me", response_model=UserResponseSchema)
async def get_me(current_user: User = Depends(get_current_user)):
    """
    Get the profile of the currently authenticated user.
    Requires a valid JWT token.
    """
    return current_user


# ---------------------------
# Public User Lookup
# ---------------------------
@router.get("/{user_id}", response_model=UserResponseSchema)
async def get_user_by_id(user_id: PyObjectId):
    """
    Get a user profile by their unique user ID.
    Public endpoint (no authentication required).
    """
    user = await User.get(user_id)
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    return user


# ---------------------------
# Update Current User Profile
# ---------------------------
@router.patch("/me", response_model=UserResponseSchema)
async def update_user_profile(
    updates: UpdateUserSchema,
    current_user: User = Depends(get_current_user)
):
    """
    Update the profile of the currently authenticated user.
    Validates username uniqueness and max length.
    """
    update_data = updates.model_dump(exclude_unset=True)

    # Username validation
    if "username" in update_data:
        update_data["username"] = update_data["username"].strip()
        if len(update_data["username"]) > 30:
            raise HTTPException(status_code=400, detail="Username too long (max 30 chars)")
        existing_user = await User.find_one(User.username == update_data["username"])
        if existing_user and existing_user.id != current_user.id:
            raise HTTPException(status_code=409, detail="Username already taken")

    # Apply updates
    for field, value in update_data.items():
        setattr(current_user, field, value)

    await current_user.save()
    return current_user


# ---------------------------
# Get User by Username
# ---------------------------
@router.get("/username/{username}", response_model=MinimalUserSchema)
async def get_user_by_username(username: str):
    """
    Retrieve minimal profile information of a user by their username.
    """
    user = await User.find_one(User.username == username.strip())
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    return user


# ---------------------------
# Followers and Following
# ---------------------------
@router.get("/{user_id}/followers", response_model=List[MinimalUserSchema])
async def get_user_followers(
    user_id: PyObjectId,
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
):
    """
    Get a paginated list of followers for a given user.
    """
    user = await User.get(user_id)
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    followers = await User.find(In(User.id, user.followers)).skip(skip).limit(limit).project(MinimalUserSchema).to_list()
    return followers


@router.get("/{user_id}/following", response_model=List[MinimalUserSchema])
async def get_user_following(
    user_id: PyObjectId,
    skip: int = Query(0, ge=0),
    limit: int = Query(20, ge=1, le=100),
):
    """
    Get a paginated list of users that a given user is following.
    """
    user = await User.get(user_id)
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    following = await User.find(In(User.id, user.following)).skip(skip).limit(limit).project(MinimalUserSchema).to_list()
    return following


# ---------------------------
# Follow / Unfollow
# ---------------------------
@router.post("/follow/{target_id}")
async def follow_user(
    target_id: PyObjectId,
    current_user: User = Depends(get_current_user)
):
    """
    Follow another user.
    - Prevents following yourself.
    - Prevents duplicate follows.
    """
    if current_user.id == target_id:
        raise HTTPException(status_code=400, detail="You cannot follow yourself.")

    target = await User.get(target_id)
    if not target:
        raise HTTPException(status_code=404, detail="User not found.")

    if target_id in current_user.following:
        raise HTTPException(status_code=400, detail="Already following this user.")

    current_user.following.append(target_id)
    target.followers.append(current_user.id)
    await current_user.save()
    await target.save()

    return {"message": f"{current_user.username} is now following {target.username}."}


@router.delete("/follow/{target_id}")
async def unfollow_user(
    target_id: PyObjectId,
    current_user: User = Depends(get_current_user)
):
    """
    Unfollow a user.
    - Prevents unfollowing yourself.
    - Prevents unfollowing someone you are not following.
    """
    if current_user.id == target_id:
        raise HTTPException(status_code=400, detail="You cannot unfollow yourself.")

    target = await User.get(target_id)
    if not target:
        raise HTTPException(status_code=404, detail="User not found.")

    if target_id not in current_user.following:
        raise HTTPException(status_code=400, detail="Not following this user.")

    current_user.following.remove(target_id)
    target.followers.remove(current_user.id)
    await current_user.save()
    await target.save()

    return {"message": f"{current_user.username} has unfollowed {target.username}."}
