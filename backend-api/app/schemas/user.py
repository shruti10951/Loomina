from pydantic import BaseModel, EmailStr, HttpUrl
from typing import Optional, List
from datetime import datetime


# For creating a new user
class CreateUserSchema(BaseModel):
    username: str
    email: EmailStr
    password: str  # plaintext; hash it before saving

    bio: Optional[str] = ""
    userProfileImage: Optional[HttpUrl] = None
    favouriteGenres: Optional[List[str]] = []
    favouriteTags: Optional[List[str]] = []



# For updating user profile
class UpdateUserSchema(BaseModel):
    username: Optional[str] = None
    bio: Optional[str] = None
    userProfileImage: Optional[HttpUrl] = None
    favouriteGenres: Optional[List[str]] = None
    favouriteTags: Optional[List[str]] = None



# For returning user data in responses (excluding sensitive fields)
class UserResponseSchema(BaseModel):
    id: str
    username: str
    email: EmailStr
    bio: Optional[str]
    userProfileImage: Optional[HttpUrl]
    favouriteGenres: List[str]
    favouriteTags: List[str]
    followers: List[str]
    following: List[str]
    creationTime: datetime

    class Config:
        orm_mode = True  # allows conversion from Beanie/Mongo documents


# For returning just username and profile image
class MinimalUserSchema(BaseModel):
    id: str
    username: str
    userProfileImage: Optional[HttpUrl] = None

    class Config:
        orm_mode = True
