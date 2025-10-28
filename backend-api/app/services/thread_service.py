from fastapi import HTTPException
from beanie import PydanticObjectId
from typing import List, Optional

from app.models.thread import Thread
from app.models.spark import Spark
from app.models.user import User

from app.schemas.thread import ThreadResponseSchema
from app.schemas.spark import SparkResponseSchema

from app.services.spark_service import build_spark_response
from app.core.constants import DEFAULT_COVER_IMAGE


# ---------------------------
# Create Thread
# ---------------------------
async def validate_and_create_thread(thread_data, current_user: User) -> Thread:
    if not current_user:
        raise HTTPException(status_code=404, detail="User not found")

    if not thread_data.threadTitle.strip():
        raise HTTPException(status_code=400, detail="Thread title cannot be empty")

    cover_image = thread_data.coverImage or DEFAULT_COVER_IMAGE

    thread = Thread(
        threadTitle=thread_data.threadTitle.strip(),
        prompt=thread_data.prompt.strip() if thread_data.prompt else "",
        userId=current_user.id,
        coverImage=cover_image,
        genre=thread_data.genre,
        tags=thread_data.tags,
    )
    await thread.insert()
    return thread


# ---------------------------
# Build Thread Response
# ---------------------------
def build_thread_response(thread: Thread, user: User, current_user: Optional[User] = None) -> ThreadResponseSchema:
    liked_by_current_user = str(current_user.id) in thread.likedBy if current_user else False

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
        likedByCurrentUser=liked_by_current_user
    )


# ---------------------------
# Get Threads by User
# ---------------------------
async def get_user_threads(current_user: User) -> List[ThreadResponseSchema]:
    threads = await Thread.find(Thread.userId == current_user.id).to_list()
    return [build_thread_response(t, current_user, current_user) for t in threads]


# ---------------------------
# Get Thread by ID
# ---------------------------
async def get_thread_by_id_service(thread_id: str, current_user: User) -> ThreadResponseSchema:
    try:
        thread_obj_id = PydanticObjectId(thread_id)
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid thread ID format")

    thread = await Thread.get(thread_obj_id)
    if not thread:
        raise HTTPException(status_code=404, detail="Thread not found")

    thread_user = await User.get(thread.userId)
    if not thread_user:
        raise HTTPException(status_code=404, detail="Thread creator not found")

    return build_thread_response(thread, thread_user, current_user)


# ---------------------------
# Ordering Logic (Pure)
# ---------------------------
def get_ordered_sparks_from_list(sparks: List[Spark]) -> List[Spark]:
    """Return sparks in logical reading order from an in-memory list."""
    if not sparks:
        return []

    def _ts(dt):
        try:
            return dt.timestamp()
        except Exception:
            return 0

    start_candidates = [s for s in sparks if s.isStart]
    if start_candidates:
        root = max(start_candidates, key=lambda s: (s.numberOfLikes, -_ts(s.creationTime)))
    else:
        no_prev = [s for s in sparks if not s.previousSparkId]
        if no_prev:
            root = max(no_prev, key=lambda s: (s.numberOfLikes, -_ts(s.creationTime)))
        else:
            root = min(sparks, key=lambda s: (_ts(s.creationTime), -s.numberOfLikes))

    ordered_sparks = []
    visited = set()
    current = root

    while current and str(current.id) not in visited:
        ordered_sparks.append(current)
        visited.add(str(current.id))

        children = [s for s in sparks if s.previousSparkId == str(current.id)]
        if not children:
            break

        current = max(children, key=lambda s: (s.numberOfLikes, -_ts(s.creationTime)))

    return ordered_sparks


# ---------------------------
# Get Thread Sparks with User Enrichment
# ---------------------------
async def get_thread_sparks_with_users(thread_id: str, current_user: User) -> List[SparkResponseSchema]:
    try:
        thread_obj_id = PydanticObjectId(thread_id)
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid thread ID format")

    thread = await Thread.get(thread_obj_id)
    if not thread:
        raise HTTPException(status_code=404, detail="Thread not found")

    sparks = await Spark.find(Spark.threadId == thread_obj_id).to_list()
    if not sparks:
        return []

    ordered_sparks = get_ordered_sparks_from_list(sparks)

    user_ids = list({str(s.userId) for s in ordered_sparks})
    user_ids_obj = [PydanticObjectId(uid) for uid in user_ids]
    users_map = {}

    if user_ids_obj:
        users = await User.find({"_id": {"$in": user_ids_obj}}).to_list()
        users_map = {str(u.id): u for u in users}

    response = []
    for s in ordered_sparks:
        u = users_map.get(str(s.userId))
        if not u:
            continue  # skip if user deleted
        response.append(await build_spark_response(s, u, current_user))

    return response


