# from fastapi import APIRouter, HTTPException
# from app.models.user import User
# from app.schemas.user import (
#     CreateUserSchema,
#     UpdateUserSchema,
#     UserResponseSchema,
#     MinimalUserSchema,
# )
# from app.core.object_id import PyObjectId
# from app.utils.hashing import hash_password
# from typing import List

# from beanie.operators import In 
# from beanie.operators import Push, Pull


# router = APIRouter(prefix="/users", tags=["Users"])


# # Register a new user
# @router.post("/", response_model=UserResponseSchema, status_code=201)
# async def register_user(user_data: CreateUserSchema):
#     # Check for duplicates
#     if await User.find_one(User.username == user_data.username):
#         raise HTTPException(status_code=409, detail="Username already taken")
#     if await User.find_one(User.email == user_data.email):
#         raise HTTPException(status_code=409, detail="Email already registered")

#     # Hash the password
#     hashed_pwd = hash_password(user_data.password)

#     # Use default profile image if none provided
#     profile_image = user_data.userProfileImage or "https://images.alphacoders.com/135/1350043.png"

#     # Create user
#     user = User(
#         username=user_data.username,
#         email=user_data.email,
#         password=hashed_pwd,
#         bio=user_data.bio,
#         userProfileImage=profile_image,
#         favouriteGenres=user_data.favouriteGenres,
#         favouriteTags=user_data.favouriteTags,
#     )

#     await user.insert()
#     return user  # orm_mode handles conversion


# # Get user by ID
# @router.get("/{user_id}", response_model=UserResponseSchema)
# async def get_user_by_id(user_id: PyObjectId):
#     user = await User.get(user_id)
#     if not user:
#         raise HTTPException(status_code=404, detail="User not found")
#     return user


# # Update user profile
# @router.patch("/{user_id}", response_model=UserResponseSchema)
# async def update_user_profile(user_id: PyObjectId, updates: UpdateUserSchema):
#     user = await User.get(user_id)
#     if not user:
#         raise HTTPException(status_code=404, detail="User not found")

#     update_data = updates.model_dump(exclude_unset=True)

#     # Username uniqueness check
#     if "username" in update_data:
#         existing_user = await User.find_one(User.username == update_data["username"])
#         if existing_user and existing_user.id != user.id:
#             raise HTTPException(status_code=409, detail="Username already taken")

#     for field, value in update_data.items():
#         setattr(user, field, value)

#     await user.save()
#     return user


# # Get user by username
# @router.get("/username/{username}", response_model=MinimalUserSchema)
# async def get_user_by_username(username: str):
#     user = await User.find_one(User.username == username)
#     if not user:
#         raise HTTPException(status_code=404, detail="User not found")
#     return user


# # Get followers
# @router.get("/{user_id}/followers")
# async def get_user_followers(user_id: PyObjectId):
#     user = await User.get(user_id)
#     if not user:
#         raise HTTPException(status_code=404, detail="User not found")
    
#     followers = await User.find(In(User.id, user.followers)).to_list()
#     return followers



# # Get following
# @router.get("/{user_id}/following", response_model=List[MinimalUserSchema])
# async def get_user_following(user_id: PyObjectId):
#     user = await User.get(user_id)
#     if not user:
#         raise HTTPException(status_code=404, detail="User not found")

#     following = await User.find(In(User.id, user.following)).to_list()
#     return following



# # Follow a user
# @router.post("/{user_id}/follow/{target_id}")
# async def follow_user(user_id: PyObjectId, target_id: PyObjectId):
#     if user_id == target_id:
#         raise HTTPException(status_code=400, detail="You cannot follow yourself.")

#     user = await User.get(user_id)
#     target = await User.get(target_id)

#     if not user or not target:
#         raise HTTPException(status_code=404, detail="User not found.")

#     if target_id in user.following:
#         raise HTTPException(status_code=400, detail="Already following this user.")

#     # Update both sides
#     await user.update(Push({User.following: target_id}))
#     await target.update(Push({User.followers: user_id}))

#     return {"message": f"{user.username} is now following {target.username}."}


# # Unfollow a user
# @router.delete("/{user_id}/follow/{target_id}")
# async def unfollow_user(user_id: PyObjectId, target_id: PyObjectId):
#     if user_id == target_id:
#         raise HTTPException(status_code=400, detail="You cannot unfollow yourself.")

#     user = await User.get(user_id)
#     target = await User.get(target_id)

#     if not user or not target:
#         raise HTTPException(status_code=404, detail="User not found.")

#     if target_id not in user.following:
#         raise HTTPException(status_code=400, detail="Not following this user.")

#     # Remove both sides
#     await user.update(Pull({User.following: target_id}))
#     await target.update(Pull({User.followers: user_id}))

#     return {"message": f"{user.username} has unfollowed {target.username}."}



from fastapi import APIRouter, HTTPException, Query
from app.models.user import User
from app.schemas.user import (
    CreateUserSchema,
    UpdateUserSchema,
    UserResponseSchema,
    MinimalUserSchema,
)
from app.core.object_id import PyObjectId
from app.utils.hashing import hash_password
from typing import List
from beanie.operators import In, Push, Pull
from beanie import PydanticObjectId

router = APIRouter(prefix="/users", tags=["Users"])

# Register a new user
@router.post("/", response_model=UserResponseSchema, status_code=201)
async def register_user(user_data: CreateUserSchema):
    # Trim whitespace
    user_data.username = user_data.username.strip()

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

    hashed_pwd = hash_password(user_data.password)
    profile_image = user_data.userProfileImage or "https://images.alphacoders.com/135/1350043.png"

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
    return user


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
