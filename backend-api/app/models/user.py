from beanie import Document
from pydantic import EmailStr, Field, HttpUrl
from datetime import datetime
from typing import List, Optional

class User(Document):
    username: str
    email: EmailStr
    password: str  # hashed
    bio: Optional[str] = ""
    userProfileImage: HttpUrl = (
        "https://images.alphacoders.com/135/1350043.png"
    )
    favouriteGenres: List[str] = []
    favouriteTags: List[str] = []
    followers: List[str] = []   # userIds
    following: List[str] = []   # userIds
    creationTime: datetime = Field(default_factory=datetime.utcnow)

    class Settings:
        name = "users"
