from beanie import Document, Link
from pydantic import Field
from typing import Dict, List
from app.models.user import User

class UserInterestProfile(Document):
    userId: Link[User]

    genreWeights: Dict[str, float] = Field(default_factory=dict)
    tagsWeights: Dict[str, float] = Field(default_factory=dict)
    keywordVector: List[float] = Field(default_factory=list)

    class Settings:
        name = "user_interest_profiles"
