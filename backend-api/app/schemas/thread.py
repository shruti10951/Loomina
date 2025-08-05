from pydantic import BaseModel, HttpUrl, Field
from typing import List, Optional
from datetime import datetime

from .user import MinimalUserSchema  # For including user info in response


# For creating thread
class CreateThreadSchema(BaseModel):
    threadTitle: str
    prompt: str
    userId: str  # user id from frontend
    coverImage: Optional[HttpUrl] = (
        "https://upload-os-bbs.hoyolab.com/upload/2025/04/18/439484087/0d2777c9ca876006a8f6c1dc91bad459_5498304819686494197.webp?x-oss-process=image%2Fresize%2Cs_1000%2Fauto-orient%2C0%2Finterlace%2C1%2Fformat%2Cwebp%2Fquality%2Cq_70"
    )
    genre: Optional[List[str]] = []
    tags: Optional[List[str]] = []


# For updating thread
class UpdateThreadSchema(BaseModel):
    threadTitle: Optional[str] = None
    prompt: Optional[str] = None
    coverImage: Optional[HttpUrl] = None
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
    coverImage: HttpUrl
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
    coverImage: HttpUrl

    class Config:
        orm_mode = True
        allow_population_by_field_name = True
