from beanie import Document, PydanticObjectId
from pydantic import Field
from datetime import datetime
from typing import List

class Thread(Document):
    threadTitle: str
    creationTime: datetime = Field(default_factory=datetime.utcnow)
    userId: PydanticObjectId  # <-- store just ObjectId, not Link[User]
    
    numberOfLikes: int = 0
    numberOfComments: int = 0
    likedBy: List[str] = []

    prompt: str
    coverImage: str
    genre: List[str] = []
    tags: List[str] = []

    reportCount: int = 0

    class Settings:
        name = "threads"
        indexes = ["userId", "creationTime", "tags", "genre"]
