from beanie import Document, Link
from pydantic import Field
from datetime import datetime

from app.models.user import User
from app.models.story import Story
from app.models.chapter import Chapter

class StoryProgress(Document):
    userId: Link[User]
    storyId: Link[Story]
    currentChapterId: Link[Chapter]

    lastAccessTime: datetime = Field(default_factory=datetime.utcnow)

    class Settings:
        name = "story_progress"
