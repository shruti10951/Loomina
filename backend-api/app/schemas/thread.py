from pydantic import BaseModel, Field
from typing import List, Optional
from datetime import datetime

from .user import MinimalUserSchema  # For including user info in response


# For creating thread
class CreateThreadSchema(BaseModel):
    threadTitle: str
    prompt: str
    coverImage: Optional[str] = None
    genre: Optional[List[str]] = []
    tags: Optional[List[str]] = []


# For updating thread
class UpdateThreadSchema(BaseModel):
    threadTitle: Optional[str] = None
    prompt: Optional[str] = None
    coverImage: Optional[str] = None
    genre: Optional[List[str]] = None
    tags: Optional[List[str]] = None


# For returing thread data in response 
class ThreadResponseSchema(BaseModel):
    id: str = Field(..., alias="_id")
    threadTitle: str
    prompt: str
    creationTime: datetime
    user: MinimalUserSchema  # populated user info
    numberOfLikes: int
    numberOfComments: int
    likedBy: List[str]
    coverImage: Optional[str] = None
    genre: List[str]
    tags: List[str]
    reportCount: int

    class Config:
        orm_mode = True
        allow_population_by_field_name = True


# For returning minimal thread data
class MinimalThreadSchema(BaseModel):
    id: str = Field(..., alias="_id")
    threadTitle: str
    user: MinimalUserSchema
    creationTime: datetime
    coverImage: Optional[str] = None

    class Config:
        orm_mode = True
        allow_population_by_field_name = True
