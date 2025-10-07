from beanie import Document, Link
from pydantic import Field
from datetime import datetime
from typing import Optional

from app.models.user import User
from app.models.thread import Thread

class SparkDraft(Document):
    userId: Link[User]
    threadId: Link[Thread] 
    sparkText: str
    previousSparkId: Optional[str] = None  # Reference to prior spark if continuing a thread

    isStart: bool = False

    creationTime: datetime = Field(default_factory=datetime.utcnow)
    lastEditedAt: datetime = Field(default_factory=datetime.utcnow)

    class Settings:
        name = "spark_drafts"
