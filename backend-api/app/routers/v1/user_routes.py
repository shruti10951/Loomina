from fastapi import APIRouter, HTTPException
from beanie import PydanticObjectId
from app.models.user import User
from app.schemas.user import (
    CreateUserSchema,
    UpdateUserSchema,
    UserResponseSchema,
    MinimalUserSchema,
)
from app.utils.hashing import hash_password
from typing import List

# Create a router group for all /users endpoints
router = APIRouter(prefix="/users", tags=["Users"])

# ---------------------------------------------------------
# Register a new user
# Endpoint: POST /users/
# Body: CreateUserSchema
# Returns: Full user data excluding password
# ---------------------------------------------------------
@router.post("/", response_model=UserResponseSchema, status_code=201)
async def register_user(user_data: CreateUserSchema):
    # Check for duplicate username
    existing_user = await User.find_one(User.username == user_data.username)
    if existing_user:
        raise HTTPException(status_code=409, detail="Username already taken")
    
    # Check for duplicate email
    existing_email = await User.find_one(User.email == user_data.email)
    if existing_email:
        raise HTTPException(status_code=409, detail="Email already registered")
    
    # Hash the password before storing
    hashed_pwd = hash_password(user_data.password)

    # Create new user document
    user = User(
        username=user_data.username,
        email=user_data.email,
        password=hashed_pwd,
        bio=user_data.bio,
        userProfileImage=user_data.userProfileImage or "https://images.alphacoders.com/135/1350043.png",
        favouriteGenres=user_data.favouriteGenres or [],
        favouriteTags=user_data.favouriteTags or [],
        followers=[],
        following=[],
    )

    # Insert into MongoDB
    await user.insert()

    user_dict = user.model_dump(by_alias=True)
    user_dict["_id"] = str(user_dict["_id"])  # Convert ObjectId to str

    return UserResponseSchema(**user_dict)


# ---------------------------------------------------------
# Get user by MongoDB _id
# Endpoint: GET /users/{user_id}
# Returns: Full user data
# ---------------------------------------------------------
@router.get("/{user_id}", response_model=UserResponseSchema)
async def get_user_by_id(user_id: PydanticObjectId):
    user = await User.get(user_id)
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    return UserResponseSchema(**user.model_dump(), id=str(user.id))


# ---------------------------------------------------------
# Update user profile (partial update)
# Endpoint: PATCH /users/{user_id}
# Body: UpdateUserSchema
# Returns: Updated user data
# ---------------------------------------------------------
@router.patch("/{user_id}", response_model=UserResponseSchema)
async def update_user_profile(user_id: PydanticObjectId, updates: UpdateUserSchema):
    user = await User.get(user_id)
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    # Only include fields that are provided (exclude None)
    update_data = updates.dict(exclude_unset=True)

    # Set each provided field dynamically
    for field, value in update_data.items():
        setattr(user, field, value)

    # Save changes to DB
    await user.save()

    return UserResponseSchema(**user.model_dump(), id=str(user.id))


# ---------------------------------------------------------
# Get user by username
# Endpoint: GET /users/username/{username}
# Returns: Minimal user data (for profile cards, mentions, etc.)
# ---------------------------------------------------------
@router.get("/username/{username}", response_model=MinimalUserSchema)
async def get_user_by_username(username: str):
    user = await User.find_one(User.username == username)
    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    return MinimalUserSchema(**user.model_dump(), id=str(user.id))


# ---------------------------------------------------------
# Get followers of a user
# Endpoint: GET /users/{user_id}/followers
# Returns: List of MinimalUserSchema
# ---------------------------------------------------------
@router.get("/{user_id}/followers", response_model=List[MinimalUserSchema])
async def get_user_followers(user_id: PydanticObjectId):
    user = await User.get(user_id)
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    # Fetch all User documents where ID is in the user's followers list
    followers = await User.find(User.id.in_(user.followers)).to_list()

    # Convert each to MinimalUserSchema
    return [MinimalUserSchema(**f.model_dump(), id=str(f.id)) for f in followers]


# ---------------------------------------------------------
# Get following list of a user
# Endpoint: GET /users/{user_id}/following
# Returns: List of MinimalUserSchema
# ---------------------------------------------------------
@router.get("/{user_id}/following", response_model=List[MinimalUserSchema])
async def get_user_following(user_id: PydanticObjectId):
    user = await User.get(user_id)
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    # Fetch all users this user is following
    following = await User.find(User.id.in_(user.following)).to_list()

    # Return minimal profile info for each
    return [MinimalUserSchema(**f.model_dump(), id=str(f.id)) for f in following]
