from pydantic import BaseModel, Field
from typing import List, Optional
from datetime import datetime

from app.schemas.user import MinimalUserSchema
from app.schemas.story import MinimalStorySchema


# For creating chapter
class CreateChapterSchema(BaseModel):
    storyId: str
    userId: str
    chapterTitle: str
    chapterContent: str
    sequenceNumber: int
    isSensitive: Optional[bool] = False


# For updating chapter
class UpdateChapterSchema(BaseModel):
    chapterTitle: Optional[str] = None
    chapterContent: Optional[str] = None
    sequenceNumber: Optional[int] = None
    isDeleted: Optional[bool] = None
    isSensitive: Optional[bool] = None


# For returing chapter data in response 
class ChapterResponseSchema(BaseModel):
    id: str = Field(..., alias="_id")
    story: MinimalStorySchema
    user: MinimalUserSchema

    chapterTitle: str
    chapterContent: str
    sequenceNumber: int
    creationTime: datetime

    numberOfLikes: int
    numberOfComments: int
    likedBy: List[str]

    reportCount: int
    isReported: bool
    isDeleted: bool
    isSensitive: bool
    isEdited: bool

    class Config:
        orm_mode = True
        allow_population_by_field_name = True


# For returning minimal chapter data
class MinimalChapterSchema(BaseModel):
    id: str = Field(..., alias="_id")
    chapterTitle: str
    sequenceNumber: int

    class Config:
        orm_mode = True
        allow_population_by_field_name = True
