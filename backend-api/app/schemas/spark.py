from pydantic import BaseModel, Field
from typing import Optional, List, Literal
from datetime import datetime

from app.schemas.user import MinimalUserSchema


# For creating spark
class CreateSparkSchema(BaseModel):
    threadId: str
    sparkText: str
    previousSparkId: Optional[str] = None
    isStart: Optional[bool] = False
    isSensitive: Optional[bool] = False



# For updating spark
class UpdateSparkSchema(BaseModel):
    sparkText: Optional[str] = None
    isSensitive: Optional[bool] = None
    isEdited: Optional[bool] = True


# For returing spark data in response 
class SparkResponseSchema(BaseModel):
    id: str = Field(..., alias="_id")
    threadId: str
    user: MinimalUserSchema
    sparkText: str
    creationTime: datetime

    numberOfLikes: int
    numberOfComments: int
    likedBy: List[str]

    likedByCurrentUser: bool = False

    previousSparkId: Optional[str] = None

    isStart: bool
    isReported: bool
    isDeleted: bool
    isSensitive: bool
    isEdited: bool

    reportCount: int

    class Config:
        orm_mode = True
        allow_population_by_field_name = True


# For returning minimal spark data
class MinimalSparkSchema(BaseModel):
    id: str = Field(..., alias="_id")
    sparkText: str
    user: MinimalUserSchema
    creationTime: datetime

    class Config:
        orm_mode = True
        allow_population_by_field_name = True

# For returning spark like
class SparkLikeResponseSchema(BaseModel):
    sparkId: str
    action: Literal["liked", "unliked"]
    numberOfLikes: int
    likedByCurrentUser: bool



