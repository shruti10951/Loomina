from beanie import Document, PydanticObjectId
from pydantic import Field
from datetime import datetime
from typing import List

from app.models.user import User
from app.models.story import Story

class Chapter(Document):
    storyId: PydanticObjectId
    userId: PydanticObjectId

    chapterTitle: str
    chapterContent: str
    sequenceNumber: int

    creationTime: datetime = Field(default_factory=datetime.utcnow)

    numberOfLikes: int = 0
    numberOfComments: int = 0
    likedBy: List[str] = [] # Store user IDs

    isReported: bool = False
    isDeleted: bool = False
    isSensitive: bool = False
    isEdited: bool = False

    reportCount: int = 0

    class Settings:
        name = "chapters"
        indexes = ["storyId", "userId", "sequenceNumber", "creationTime"]


