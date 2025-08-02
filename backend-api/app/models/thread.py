from beanie import Document, Link
from pydantic import Field, HttpUrl
from datetime import datetime
from typing import List

from app.models.user import User  

class Thread(Document):
    threadTitle: str
    creationTime: datetime = Field(default_factory=datetime.utcnow)
    userId: Link[User]
    
    numberOfLikes: int = 0
    numberOfComments: int = 0
    likedBy: List[str] = []  # List of User IDs

    prompt: str
    coverImage: HttpUrl = (
        "https://upload-os-bbs.hoyolab.com/upload/2025/04/18/439484087/0d2777c9ca876006a8f6c1dc91bad459_5498304819686494197.webp?x-oss-process=image%2Fresize%2Cs_1000%2Fauto-orient%2C0%2Finterlace%2C1%2Fformat%2Cwebp%2Fquality%2Cq_70"
    )
    genre: List[str] = []
    tags: List[str] = []

    reportCount: int = 0

    class Settings:
        name = "threads"
