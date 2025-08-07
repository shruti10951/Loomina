from pydantic import BaseModel, Field
from datetime import datetime


# For returning thread progress
class ThreadProgressResponse(BaseModel):
    id: str = Field(..., alias="_id")
    userId: str
    threadId: str
    currentSparkId: str
    lastAccessTime: datetime
