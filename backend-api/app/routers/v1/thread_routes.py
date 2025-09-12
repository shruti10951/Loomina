from fastapi import APIRouter, HTTPException, Depends
from beanie import PydanticObjectId

from app.models.thread import Thread
from app.models.user import User
from app.schemas.thread import CreateThreadSchema, ThreadResponseSchema
from app.utils.security import get_current_user

# Initialize API Router
router = APIRouter()


# ---------------------------
# Create Thread
# ---------------------------
@router.post("/create", response_model=ThreadResponseSchema)
async def create_thread(
    thread_data: CreateThreadSchema,
    current_user: User = Depends(get_current_user)  # Extract user from token
    ):
    
    # user is automatically validated from token
    user = current_user

    if not user:
        raise HTTPException(status_code=404, detail="User not found")
    

    
    # for now hardcoding image url for all threads
    coverImage = "https://i.pinimg.com/736x/80/1e/15/801e15facd363f7025931f4c6c1eb0d2.jpg"

    # Create new thread document
    thread = Thread(
        threadTitle=thread_data.threadTitle,
        prompt=thread_data.prompt,
        userId=user,
        coverImage=coverImage,
        genre=thread_data.genre,
        tags=thread_data.tags,
    )
    await thread.insert()

    # Build response
    return ThreadResponseSchema(
        _id=str(thread.id),
        threadTitle=thread.threadTitle,
        prompt=thread.prompt,
        creationTime=thread.creationTime,
        user={
            "_id": str(user.id),
            "username": user.username,
            "userProfileImage": user.userProfileImage,
        },
        numberOfLikes=thread.numberOfLikes,
        numberOfComments=thread.numberOfComments,
        likedBy=thread.likedBy,
        coverImage=thread.coverImage,
        genre=thread.genre,
        tags=thread.tags,
        reportCount=thread.reportCount,
    )
