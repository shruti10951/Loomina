from beanie import Document, Link
from pydantic import Field, Literal
from datetime import datetime
from typing import Optional

from app.models.user import User

class Report(Document):
    userId: Link[User]  # Who reported
    targetId: str       # ID of the reported object
    targetType: Literal["spark", "thread", "story", "chapter", "comment"]  # What was reported

    reason: Literal["spam", "abuse", "explicit", "plagiarism", "other"]  # Allow more precise moderation
    details: Optional[str] = None  # Optional explanation

    timestamp: datetime = Field(default_factory=datetime.utcnow)

    class Settings:
        name = "reports"
