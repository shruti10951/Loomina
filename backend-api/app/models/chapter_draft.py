from beanie import Document, Link
from pydantic import Field
from datetime import datetime
from typing import Optional

from app.models.user import User
from app.models.story import Story

class ChapterDraft(Document):
    userId: Link[User]
    storyId: Link[Story]

    chapterTitle: str
    chapterContent: str
    sequenceNumber: Optional[int] = None  # Optional until finalized

    creationTime: datetime = Field(default_factory=datetime.utcnow)
    lastEditedAt: datetime = Field(default_factory=datetime.utcnow)

    class Settings:
        name = "chapter_drafts"
