from beanie import Document, Link
from pydantic import Field
from datetime import datetime
from typing import List, Optional

from app.models.user import User
from app.models.thread import Thread

class Spark(Document):
    threadId: Link[Thread]
    userId: Link[User]

    sparkText: str
    creationTime: datetime = Field(default_factory=datetime.utcnow)

    numberOfLikes: int = 0
    numberOfComments: int = 0
    likedBy: List[str] = []  # Store user IDs

    previousSparkId: Optional[str] = None  # ID of previous spark in thread (for threaded navigation)

    isStart: bool = False
    isReported: bool = False
    isDeleted: bool = False
    isSensitive: bool = False
    isEdited: bool = False

    reportCount: int = 0

    class Settings:
        name = "sparks"
