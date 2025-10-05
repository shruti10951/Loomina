from fastapi import APIRouter, HTTPException, Depends
from beanie import PydanticObjectId

from app.models.thread import Thread
from app.models.user import User
from app.models.spark import Spark

from app.schemas.thread import CreateThreadSchema, ThreadResponseSchema
from app.schemas.spark import SparkResponseSchema

from app.utils.security import get_current_user
from app.services.thread_service import get_ordered_sparks


from typing import List


# Initialize API Router
router = APIRouter()


# ---------------------------
# Create Thread
# ---------------------------
@router.post("/", response_model=ThreadResponseSchema)
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
        likedByCurrentUser=False
    )


@router.get("/me", response_model=List[ThreadResponseSchema])
async def get_my_threads(current_user: User = Depends(get_current_user)):
    threads = await Thread.find(Thread.userId == current_user.id).to_list()

    results = []
    for t in threads:

        liked_by_current_user = str(current_user.id) in t.likedBy

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
                likedByCurrentUser=liked_by_current_user
            )
        )
    return results


# ---------------------------
# Get Thread by ID
# ---------------------------
@router.get("/{thread_id}", response_model=ThreadResponseSchema)
async def get_thread_by_id(thread_id: str, current_user: User = Depends(get_current_user)):
    try:
        thread_obj_id = PydanticObjectId(thread_id)
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid thread ID format")

    thread = await Thread.get(thread_obj_id)
    if not thread:
        raise HTTPException(status_code=404, detail="Thread not found")

    # Fetch user info for the thread creator
    thread_user = await User.get(thread.userId)
    if not thread_user:
        raise HTTPException(status_code=404, detail="Thread creator not found")
    
    liked_by_current_user = str(current_user.id) in thread.likedBy

    return ThreadResponseSchema(
        _id=str(thread.id),
        threadTitle=thread.threadTitle,
        prompt=thread.prompt,
        creationTime=thread.creationTime,
        user={
            "_id": str(thread_user.id),
            "username": thread_user.username,
            "userProfileImage": thread_user.userProfileImage,
        },
        numberOfLikes=thread.numberOfLikes,
        numberOfComments=thread.numberOfComments,
        likedBy=thread.likedBy,
        coverImage=thread.coverImage,
        genre=thread.genre,
        tags=thread.tags,
        reportCount=thread.reportCount,
        likedByCurrentUser=liked_by_current_user
    )


# ---------------------------
# Get Ordered Sparks for a Thread
# ---------------------------
@router.get("/{thread_id}/sparks/ordered", response_model=List[SparkResponseSchema])
async def fetch_ordered_sparks(
    thread_id: str,
    current_user: User = Depends(get_current_user)
):
    try:
        thread_obj_id = PydanticObjectId(thread_id)
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid thread ID format")
    
    # get ordered sparks from service
    ordered_sparks: List[Spark] = await get_ordered_sparks(thread_obj_id)

    # enrich author info for each spark
    user_ids = list({str(s.userId) for s in ordered_sparks})
    user_ids_obj = [PydanticObjectId(uid) for uid in user_ids]

    users_map = {}
    if user_ids_obj:
        users = await User.find({"_id": {"$in": user_ids_obj}}).to_list()
        users_map = {str(u.id): u for u in users}

    # Build response list
    response = []
    current_user_id_str = str(current_user.id)
    for s in ordered_sparks:
        u = users_map.get(str(s.userId))

        liked_by_current_user = current_user_id_str in s.likedBy

        response.append(
            SparkResponseSchema(
                _id = str(s.id),
                threadId = str(s.threadId),
                user = {
                    "_id" : str(s.userId),
                    "username" : u.username if u else "",
                    "userProfileImage" : u.userProfileImage if u else ""
                },
                sparkText = s.sparkText,
                creationTime= s.creationTime,
                numberOfLikes= s.numberOfLikes,
                numberOfComments= s.numberOfComments,
                likedBy= s.likedBy,
                previousSparkId= s.previousSparkId,
                isStart= s.isStart,
                isReported= s.isReported,
                isDeleted= s.isDeleted,
                isSensitive= s.isSensitive,
                isEdited= s.isEdited,
                reportCount= s.reportCount,
                likedByCurrentUser=liked_by_current_user
            )
        )
    
    return response


