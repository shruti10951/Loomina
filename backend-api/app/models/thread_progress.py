from beanie import Document, Link
from pydantic import Field
from datetime import datetime

from app.models.user import User
from app.models.thread import Thread
from app.models.spark import Spark

class ThreadProgress(Document):
    userId: Link[User]
    threadId: Link[Thread]
    currentSparkId: Link[Spark]

    lastAccessTime: datetime = Field(default_factory=datetime.utcnow)

    class Settings:
        name = "thread_progress"
