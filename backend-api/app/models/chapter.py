from beanie import Document, Link
from pydantic import Field
from datetime import datetime
from typing import List

from app.models.user import User
from app.models.story import Story

class Chapter(Document):
    storyId: Link[Story]
    userId: Link[User]

    chapterTitle: str
    chapterContent: str
    sequenceNumber: int

    creationTime: datetime = Field(default_factory=datetime.utcnow)

    numberOfLikes: int = 0
    numberOfComments: int = 0
    likedBy: List[str] = []

    reportCount: int = 0
    isReported: bool = False
    isDeleted: bool = False
    isSensitive: bool = False
    isEdited: bool = False

    class Settings:
        name = "chapters"
        indexes = ["storyId", "userId", "sequenceNumber", "creationTime"]


