from beanie import Document, PydanticObjectId
from pydantic import Field, HttpUrl
from datetime import datetime
from typing import List

from app.models.user import User

class Story(Document):
    storyTitle: str
    creationTime: datetime = Field(default_factory=datetime.utcnow)
    userId: PydanticObjectId 

    numberOfLikes: int = 0
    numberOfComments: int = 0
    likedBy: List[str] = []
    
    storySynopsis: str
    coverImage: str
    genre: List[str] = []
    tags: List[str] = []  

    isCompleted: bool = False
    reportCount: int = 0

    class Settings:
        name = "stories"
        indexes = ["userId", "creationTime", "genre", "tags"]

