from pydantic import BaseModel, Field, HttpUrl
from typing import List, Optional
from datetime import datetime

from app.schemas.user import MinimalUserSchema


# For creating story
class CreateStorySchema(BaseModel):
    userId: str
    storyTitle: str
    storySynopsis: str
    genre: Optional[List[str]] = []
    tags: Optional[List[str]] = []
    coverImage: Optional[HttpUrl] = (
        "https://cdn.pixabay.com/photo/2017/08/30/07/52/fantasy-2695569_960_720.jpg"
    )


# For updating story
class UpdateStorySchema(BaseModel):
    storyTitle: Optional[str] = None
    storySynopsis: Optional[str] = None
    genre: Optional[List[str]] = None
    tags: Optional[List[str]] = None
    coverImage: Optional[HttpUrl] = None
    isCompleted: Optional[bool] = None


# For returing story data in response 
class StoryResponseSchema(BaseModel):
    id: str = Field(..., alias="_id")
    user: MinimalUserSchema
    storyTitle: str
    storySynopsis: str
    genre: List[str]
    tags: List[str]
    coverImage: HttpUrl
    creationTime: datetime
    numberOfLikes: int
    numberOfComments: int
    isCompleted: bool
    reportCount: int

    class Config:
        orm_mode = True
        allow_population_by_field_name = True


# For returning minimal story data
class MinimalStorySchema(BaseModel):
    id: str = Field(..., alias="_id")
    storyTitle: str
    coverImage: HttpUrl
    user: MinimalUserSchema

    class Config:
        orm_mode = True
        allow_population_by_field_name = True
