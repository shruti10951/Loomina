from pydantic import BaseModel, Field
from typing import List, Optional, Literal
from datetime import datetime

from app.schemas.user import MinimalUserSchema


# For creating a comment
class CreateCommentSchema(BaseModel):
    parentType: Literal["spark", "thread", "story", "chapter"]
    parentId: str
    userId: str
    commentText: str
    isSpoiler: Optional[bool] = False


# For updating a comment
class UpdateCommentSchema(BaseModel):
    commentText: Optional[str] = None
    isSpoiler: Optional[bool] = None
    isDeleted: Optional[bool] = None


# For returning full comment data in responses
class CommentResponseSchema(BaseModel):
    id: str = Field(..., alias="_id")
    parentType: str
    parentId: str
    user: MinimalUserSchema

    commentText: str
    creationTime: datetime

    likedBy: List[str]
    numberOfLikes: int

    isDeleted: bool
    isSpoiler: bool
    isEdited: bool

    class Config:
        orm_mode = True
        allow_population_by_field_name = True


# For returning minimal comment info
class MinimalCommentSchema(BaseModel):
    id: str = Field(..., alias="_id")
    commentText: str
    user: MinimalUserSchema

    class Config:
        orm_mode = True
        allow_population_by_field_name = True
