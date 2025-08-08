from pydantic import BaseModel, EmailStr, HttpUrl, Field
from typing import Optional, List
from datetime import datetime

from app.core.object_id import PyObjectId

# For creating a new user
class CreateUserSchema(BaseModel):
    username: str
    email: EmailStr
    password: str  # plaintext; hash it before saving

    bio: Optional[str] = ""
    userProfileImage: Optional[str] = None
    favouriteGenres: Optional[List[str]] = []
    favouriteTags: Optional[List[str]] = []



# For updating user profile
class UpdateUserSchema(BaseModel):
    username: Optional[str] = None
    bio: Optional[str] = None
    userProfileImage: Optional[str] = None
    favouriteGenres: Optional[List[str]] = None
    favouriteTags: Optional[List[str]] = None



# For returning user data in responses (excluding sensitive fields)
class UserResponseSchema(BaseModel):
    id: PyObjectId = Field(..., alias="_id")
    username: str
    email: EmailStr
    bio: Optional[str]
    userProfileImage: Optional[str]
    favouriteGenres: List[str]
    favouriteTags: List[str]
    followers: List[str]
    following: List[str]
    creationTime: datetime

    class Config:
        populate_by_name = True  # Allows using 'id' instead of '_id'
        orm_mode = True  # allows conversion from Beanie/Mongo documents


# For returning just username and profile image
class MinimalUserSchema(BaseModel):
    id: PyObjectId = Field(..., alias="_id")
    username: str
    userProfileImage: Optional[str] = None

    class Config:
        orm_mode = True
