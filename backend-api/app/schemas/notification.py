from pydantic import BaseModel, Field
from typing import Optional, Literal
from datetime import datetime

from app.schemas.user import MinimalUserSchema


# Schema for creating a notification (usually triggered by backend logic)
class CreateNotificationSchema(BaseModel):
    userId: str
    type: Literal["like", "comment", "mention", "follow", "report"]
    targetType: Literal["spark", "thread", "story", "chapter", "comment", "user"]
    targetId: str
    message: str


# Full response schema for returning notification details to the user
class NotificationResponseSchema(BaseModel):
    id: str = Field(..., alias="_id")
    type: str
    targetType: str
    targetId: str
    message: str
    timestamp: datetime
    seenAt: Optional[datetime]
    user: MinimalUserSchema

    class Config:
        orm_mode = True
        allow_population_by_field_name = True


# Optional: for minimal listing (e.g., in compact UI)
class MinimalNotificationSchema(BaseModel):
    id: str = Field(..., alias="_id")
    message: str
    timestamp: datetime
    seenAt: Optional[datetime]

    class Config:
        orm_mode = True
        allow_population_by_field_name = True
