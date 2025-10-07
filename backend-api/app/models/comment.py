from beanie import Document, Link
from pydantic import Field
from datetime import datetime
from typing import List, Literal

from app.models.user import User

class Comment(Document):
    parentType: Literal["spark", "thread", "story", "chapter"]
    parentId: str  # ObjectId as string reference
    userId: Link[User]

    commentText: str
    creationTime: datetime = Field(default_factory=datetime.utcnow)

    likedBy: List[str] = []
    numberOfLikes: int = 0

    isDeleted: bool = False
    isSpoiler: bool = False
    isEdited: bool = False

    class Settings:
        name = "comments"
        indexes = [("parentType", "parentId"), "userId", "creationTime"]

