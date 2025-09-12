from beanie import Document, Link
from pydantic import Field
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
    coverImage: str
    genre: List[str] = []
    tags: List[str] = []

    reportCount: int = 0

    class Settings:
        name = "threads"
        indexes = ["userId", "creationTime", "tags", "genre"]

