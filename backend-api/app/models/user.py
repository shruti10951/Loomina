from typing import List, Optional
from datetime import datetime
from beanie import Document

from pydantic import EmailStr, Field
from app.core.object_id import PyObjectId  # ✅ use our custom ObjectId

class User(Document):
    username: str
    email: EmailStr
    password: str
    bio: Optional[str] = ""
    userProfileImage: str
    favouriteGenres: List[str] = []
    favouriteTags: List[str] = []
    followers: List[PyObjectId] = []   # ✅ auto-conversion
    following: List[PyObjectId] = []   # ✅ auto-conversion
    creationTime: datetime = Field(default_factory=datetime.utcnow)

    class Settings:
        name = "users"
