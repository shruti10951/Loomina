from pydantic import BaseModel, Field
from typing import Optional, Literal
from datetime import datetime

from app.schemas.user import MinimalUserSchema


# For creating a report
class CreateReportSchema(BaseModel):
    userId: str
    targetId: str
    targetType: Literal["spark", "thread", "story", "chapter", "comment"]
    reason: Literal["spam", "abuse", "explicit", "plagiarism", "other"]
    details: Optional[str] = None


# For returning report data 
class ReportResponseSchema(BaseModel):
    id: str = Field(..., alias="_id")
    user: MinimalUserSchema

    targetId: str
    targetType: str
    reason: str
    details: Optional[str]
    timestamp: datetime

    class Config:
        orm_mode = True
        allow_population_by_field_name = True


# For listing minimal reports
class MinimalReportSchema(BaseModel):
    id: str = Field(..., alias="_id")
    reason: str
    targetType: str

    class Config:
        orm_mode = True
        allow_population_by_field_name = True
