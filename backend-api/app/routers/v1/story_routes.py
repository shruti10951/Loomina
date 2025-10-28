from fastapi import APIRouter, HTTPException, Depends
from beanie import PydanticObjectId

from app.models.story import Story
from app.models.user import User

from app.schemas.story import CreateStorySchema, StoryResponseSchema

from app.services.story_service import (
    validate_and_create_story, 
    build_story_response,
    get_user_stories,
    get_story_by_id_service
    )

from app.utils.security import get_current_user

from typing import List


# Initialize API Router
router = APIRouter()


# ---------------------------
# Create Story
# ---------------------------
@router.post("/", response_model=StoryResponseSchema)
async def create_story(
    story_data: CreateStorySchema,
    current_user: User = Depends(get_current_user)
):
    story = await validate_and_create_story(story_data, current_user)    
    return await build_story_response(story, current_user)




@router.get("/me", response_model=List[StoryResponseSchema])
async def get_my_stories(current_user: User = Depends(get_current_user)):
    return await get_user_stories(current_user)

# ---------------------------
# Get Story by ID
# ---------------------------
@router.get('/{story_id}', response_model=StoryResponseSchema)
async def get_story_by_id(story_id: str, current_user: User = Depends(get_current_user)):
    return await get_story_by_id_service(story_id, current_user)


