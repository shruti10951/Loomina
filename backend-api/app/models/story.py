from beanie import Document, Link
from pydantic import Field, HttpUrl
from datetime import datetime
from typing import List

from app.models.user import User

class Story(Document):
    userId: Link[User]

    storyTitle: str
    storySynopsis: str

    genre: List[str] = []
    tags: List[str] = []

    creationTime: datetime = Field(default_factory=datetime.utcnow)

    coverImage: HttpUrl = (
        "https://cdn.pixabay.com/photo/2017/08/30/07/52/fantasy-2695569_960_720.jpg"
    )

    numberOfLikes: int = 0
    numberOfComments: int = 0

    isCompleted: bool = False
    reportCount: int = 0

    class Settings:
        name = "stories"
        indexes = ["userId", "creationTime", "genre", "tags"]

