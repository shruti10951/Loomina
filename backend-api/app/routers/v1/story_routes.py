from fastapi import APIRouter, HTTPException, Depends
from beanie import PydanticObjectId

from app.models.story import Story
from app.models.user import User
from app.schemas.story import CreateStorySchema, StoryResponseSchema
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
    if not current_user:
        raise HTTPException(status_code=404, detail="User not found")

    coverImage = story_data.coverImage or "https://i.pinimg.com/736x/80/1e/15/801e15facd363f7025931f4c6c1eb0d2.jpg"

    # Save only the ObjectId
    story = Story(
        storyTitle=story_data.storyTitle,
        storySynopsis=story_data.storySynopsis,
        userId=current_user.id,   # <-- ObjectId
        coverImage=coverImage,
        genre=story_data.genre,
        tags=story_data.tags,
    )
    await story.insert()

    return StoryResponseSchema(
        _id=str(story.id),
        storyTitle=story.storyTitle,
        storySynopsis=story.storySynopsis,
        creationTime=story.creationTime,
        user={
            "_id": str(current_user.id),
            "username": current_user.username,
            "userProfileImage": current_user.userProfileImage,
        },
        numberOfLikes=story.numberOfLikes,
        numberOfComments=story.numberOfComments,
        numberOfChapters=story.numberOfChapters,
        likedBy=story.likedBy,
        coverImage=story.coverImage,
        genre=story.genre,
        tags=story.tags,
        reportCount=story.reportCount,
        isCompleted=story.isCompleted
    )


@router.get("/me", response_model=List[StoryResponseSchema])
async def get_my_stories(current_user: User = Depends(get_current_user)):
    stories = await Story.find(Story.userId == current_user.id).to_list()

    results = []
    for story in stories:
        results.append(
            StoryResponseSchema(
                _id=str(story.id),
                storyTitle=story.storyTitle,
                storySynopsis=story.storySynopsis,
                creationTime=story.creationTime,
                user={
                    "_id": str(current_user.id),
                    "username": current_user.username,
                    "userProfileImage": current_user.userProfileImage,
                },
                numberOfLikes=story.numberOfLikes,
                numberOfComments=story.numberOfComments,
                numberOfChapters=story.numberOfChapters,
                likedBy=story.likedBy,
                coverImage=story.coverImage,
                genre=story.genre,
                tags=story.tags,
                reportCount=story.reportCount,
                isCompleted=story.isCompleted
            )
        )
    return results

# ---------------------------
# Get Story by ID
# ---------------------------
@router.get('/{story_id}', response_model=StoryResponseSchema)
async def get_story_by_id(story_id: str, current_user: User = Depends(get_current_user)):
    try:
        story_obj_id = PydanticObjectId(story_id)
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid story ID format")
    
    story = await Story.get(story_obj_id)
    if not story:
        raise HTTPException(status_code=404, detail="Story not found")
    
    # Fetch user info for the story creator
    story_user = await User.get(story.userId)
    if not story_user:
        raise HTTPException(status_code=404, detail="Story creator not found")
    
    liked_by_current_user = str(current_user.id) in story.likedBy

    return StoryResponseSchema(
        _id= str(story.id),
        storyTitle= story.storyTitle,
        storySynopsis=story.storySynopsis,
        creationTime= story.creationTime,
        user={
            "_id": str(story_user.id),
            "username": story_user.username,
            "userProfileImage": story_user.userProfileImage,
        },
        numberOfLikes= story.numberOfLikes,
        numberOfComments= story.numberOfComments,
        numberOfChapters= story.numberOfChapters,
        likedBy= story.likedBy,
        likedByCurrentUser= liked_by_current_user,
        coverImage= story.coverImage,
        genre= story.genre,
        tags= story.tags,
        isCompleted= story.isCompleted,
        reportCount= story.reportCount
    )


