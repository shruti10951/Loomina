from beanie import Document, Link
from pydantic import Field
from datetime import datetime
from typing import Optional, Literal

from app.models.user import User

class Notification(Document):
    userId: Link[User]  # Receiver of the notification

    # What action triggered this notification
    type: Literal["like", "comment", "mention", "follow", "report"]

    # What object was acted upon (e.g., Spark, Comment, Story, etc.)
    targetType: Literal["spark", "thread", "story", "chapter", "comment", "user"]
    targetId: str  # ID of the object that was liked, commented, etc.

    message: str  # UI-friendly text
    timestamp: datetime = Field(default_factory=datetime.utcnow)
    seenAt: Optional[datetime] = None

    class Settings:
        name = "notifications"
