from fastapi import APIRouter, HTTPException, Depends
from beanie import PydanticObjectId

from app.models.thread import Thread
from app.models.user import User
from app.schemas.thread import CreateThreadSchema, ThreadResponseSchema
from app.utils.security import get_current_user

from typing import List


# Initialize API Router
router = APIRouter()


# ---------------------------
# Create Thread
# ---------------------------
@router.post("/create", response_model=ThreadResponseSchema)
async def create_thread(
    thread_data: CreateThreadSchema,
    current_user: User = Depends(get_current_user)
):
    if not current_user:
        raise HTTPException(status_code=404, detail="User not found")

    coverImage = thread_data.coverImage or "https://i.pinimg.com/736x/80/1e/15/801e15facd363f7025931f4c6c1eb0d2.jpg"

    # Save only the ObjectId
    thread = Thread(
        threadTitle=thread_data.threadTitle,
        prompt=thread_data.prompt,
        userId=current_user.id,   # <-- ObjectId
        coverImage=coverImage,
        genre=thread_data.genre,
        tags=thread_data.tags,
    )
    await thread.insert()

    print(thread.id)

    return ThreadResponseSchema(
        _id=str(thread.id),
        threadTitle=thread.threadTitle,
        prompt=thread.prompt,
        creationTime=thread.creationTime,
        user={
            "_id": str(current_user.id),
            "username": current_user.username,
            "userProfileImage": current_user.userProfileImage,
        },
        numberOfLikes=thread.numberOfLikes,
        numberOfComments=thread.numberOfComments,
        likedBy=thread.likedBy,
        coverImage=thread.coverImage,
        genre=thread.genre,
        tags=thread.tags,
        reportCount=thread.reportCount,
    )


@router.get("/me", response_model=List[ThreadResponseSchema])
async def get_my_threads(current_user: User = Depends(get_current_user)):
    threads = await Thread.find(Thread.userId == current_user.id).to_list()

    results = []
    for t in threads:
        results.append(
            ThreadResponseSchema(
                _id=str(t.id),
                threadTitle=t.threadTitle,
                prompt=t.prompt,
                creationTime=t.creationTime,
                user={
                    "_id": str(current_user.id),
                    "username": current_user.username,
                    "userProfileImage": current_user.userProfileImage,
                },
                numberOfLikes=t.numberOfLikes,
                numberOfComments=t.numberOfComments,
                likedBy=t.likedBy,
                coverImage=t.coverImage,
                genre=t.genre,
                tags=t.tags,
                reportCount=t.reportCount,
            )
        )
    return results
