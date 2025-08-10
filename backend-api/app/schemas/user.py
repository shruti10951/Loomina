from pydantic import BaseModel, EmailStr, Field
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
    favouriteGenres: List[str] = Field(default_factory=list)
    favouriteTags: List[str] = Field(default_factory=list)


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
    bio: Optional[str] = None
    userProfileImage: Optional[str] = None
    favouriteGenres: List[str] = Field(default_factory=list)
    favouriteTags: List[str] = Field(default_factory=list)
    followers: List[PyObjectId] = Field(default_factory=list)
    following: List[PyObjectId] = Field(default_factory=list)
    creationTime: datetime

    class Config:
        populate_by_name = True
        orm_mode = True


# For returning just username and profile image
class MinimalUserSchema(BaseModel):
    id: PyObjectId = Field(..., alias="_id")
    username: str
    userProfileImage: Optional[str] = None

    class Config:
        populate_by_name = True
        orm_mode = True
