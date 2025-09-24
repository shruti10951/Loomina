from pydantic import BaseModel, Field
from typing import List, Optional
from datetime import datetime

from app.schemas.user import MinimalUserSchema


# For creating story
class CreateStorySchema(BaseModel):
    storyTitle: str
    storySynopsis: str
    coverImage: Optional[str] = None
    genre: Optional[List[str]] = []
    tags: Optional[List[str]] = []
    

# For updating story
class UpdateStorySchema(BaseModel):
    storyTitle: Optional[str] = None
    storySynopsis: Optional[str] = None
    coverImage: Optional[str] = None
    genre: Optional[List[str]] = None
    tags: Optional[List[str]] = None
    isCompleted: Optional[bool] = None


# For returing story data in response 
class StoryResponseSchema(BaseModel):
    id: str = Field(..., alias="_id")
    storyTitle: str
    storySynopsis: str
    creationTime: datetime
    user: MinimalUserSchema
    numberOfLikes: int
    numberOfComments: int
    likedBy: List[str]
    coverImage: Optional[str] = None
    genre: List[str]
    tags: List[str]
    isCompleted: bool
    reportCount: int

    class Config:
        orm_mode = True
        allow_population_by_field_name = True


# For returning minimal story data
class MinimalStorySchema(BaseModel):
    id: str = Field(..., alias="_id")
    storyTitle: str
    user: MinimalUserSchema
    creationTime: datetime
    coverImage: Optional[str] = None
    

    class Config:
        orm_mode = True
        allow_population_by_field_name = True
