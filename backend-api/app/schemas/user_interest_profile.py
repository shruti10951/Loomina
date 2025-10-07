from pydantic import BaseModel, Field
from typing import Dict, List

from app.schemas.user import MinimalUserSchema


# Schema for creating or updating a user's interest profile
class UpdateUserInterestProfileSchema(BaseModel):
    genreWeights: Dict[str, float] = Field(default_factory=dict)
    tagsWeights: Dict[str, float] = Field(default_factory=dict)
    keywordVector: List[float] = Field(default_factory=list)


# Response schema for returning full profile (admin/internal)
class UserInterestProfileResponseSchema(BaseModel):
    id: str = Field(..., alias="_id")
    user: MinimalUserSchema
    genreWeights: Dict[str, float]
    tagsWeights: Dict[str, float]
    keywordVector: List[float]

    class Config:
        orm_mode = True
        allow_population_by_field_name = True
