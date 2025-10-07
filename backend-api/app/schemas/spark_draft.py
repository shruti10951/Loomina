from pydantic import BaseModel, Field
from typing import Optional
from datetime import datetime


# For creating spark draft
class SparkDraftCreate(BaseModel):
    threadId: str
    sparkText: str
    previousSparkId: Optional[str] = None
    isStart: bool = False


# For updating existing draft
class SparkDraftUpdate(BaseModel):
    sparkText: Optional[str] = None
    previousSparkId: Optional[str] = None
    isStart: Optional[bool] = None


# For returning spark response
class SparkDraftResponse(BaseModel):
    id: str = Field(..., alias="_id")
    userId: str
    threadId: str
    sparkText: str
    previousSparkId: Optional[str]
    isStart: bool
    creationTime: datetime
    lastEditedAt: datetime
