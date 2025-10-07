from pydantic import BaseModel, Field
from typing import Optional
from datetime import datetime


# For creating chapter draft
class ChapterDraftCreate(BaseModel):
    storyId: str
    chapterTitle: str
    chapterContent: str
    sequenceNumber: Optional[int] = None


# For updating existing draft
class ChapterDraftUpdate(BaseModel):
    chapterTitle: Optional[str] = None
    chapterContent: Optional[str] = None
    sequenceNumber: Optional[int] = None


# For returning chapter response
class ChapterDraftResponse(BaseModel):
    id: str = Field(..., alias="_id")
    userId: str
    storyId: str
    chapterTitle: str
    chapterContent: str
    sequenceNumber: Optional[int]
    creationTime: datetime
    lastEditedAt: datetime
