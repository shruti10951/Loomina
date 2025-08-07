from pydantic import BaseModel, Field
from datetime import datetime


# For returning story progress response
class StoryProgressResponse(BaseModel):
    id: str = Field(..., alias="_id")
    userId: str
    storyId: str
    currentChapterId: str
    lastAccessTime: datetime
