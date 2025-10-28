from fastapi import HTTPException
from app.models.story import Story
from app.models.user import User
from app.schemas.story import StoryResponseSchema
from beanie import PydanticObjectId
from typing import List, Optional


DEFAULT_COVER_IMAGE = "https://i.pinimg.com/736x/80/1e/15/801e15facd363f7025931f4c6c1eb0d2.jpg"


# ---------------------------
# Create Story
# ---------------------------
async def validate_and_create_story(story_data, current_user: User) -> Story:
    if not current_user:
        raise HTTPException(status_code=404, detail="User not found")
    
    # Title validation
    if not story_data.storyTitle.strip():
        raise HTTPException(status_code=400, detail="Story title cannot be empty")
    
    # Synopsis validation
    if not story_data.storySynopsis.strip():
        raise HTTPException(status_code=400, detail="Story Synopsis cannot be empty")
    
    # Optional cover image
    cover_image = story_data.coverImage or DEFAULT_COVER_IMAGE

    # Create story document
    story = Story(
        storyTitle = story_data.storyTitle.strip(),
        storySynopsis = story_data.storySynopsis.strip(),
        userId = current_user.id,
        coverImage = cover_image,
        genre = story_data.genre,
        tags = story_data.tags,
    )

    await story.insert()
    return story


# ---------------------------
# Build Response
# ---------------------------
async def build_story_response(story: Story, user: User, current_user: Optional[User] = None) -> StoryResponseSchema:
    """Helper to build response schema."""

    liked_by_current_user = (
        str(current_user.id) in story.likedBy if current_user else False
    )

    return StoryResponseSchema(
        _id=str(story.id),
        storyTitle=story.storyTitle,
        storySynopsis=story.storySynopsis,
        creationTime=story.creationTime,
        user={
            "_id": str(user.id),
            "username": user.username,
            "userProfileImage": user.userProfileImage,
        },
        numberOfLikes=story.numberOfLikes,
        numberOfComments=story.numberOfComments,
        numberOfChapters=story.numberOfChapters,
        likedBy=story.likedBy,
        likedByCurrentUser=liked_by_current_user,
        coverImage=story.coverImage,
        genre=story.genre,
        tags=story.tags,
        reportCount=story.reportCount,
        isCompleted=story.isCompleted
    )


# ---------------------------
# Fetch My Stories
# ---------------------------
async def get_user_stories(current_user: User) -> List[StoryResponseSchema]:
    stories = await Story.find(Story.userId == current_user.id).to_list()

    return [
        await build_story_response(story, current_user)
        for story in stories
    ]

# ---------------------------
# Fetch Story by ID
# ---------------------------
async def get_story_by_id_service(story_id: str, current_user: User) -> StoryResponseSchema:
    try:
        story_obj_id = PydanticObjectId(story_id)
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid story ID format")
    
    story = await Story.get(story_obj_id)
    if not story:
        raise HTTPException(status_code=404, detail="Story not found")
    
    story_user = await User.get(story.userId)
    if not story_user:
        raise HTTPException(status_code=404, detail="Story creator not found")
    
    return await build_story_response(story, story_user, current_user=current_user)


