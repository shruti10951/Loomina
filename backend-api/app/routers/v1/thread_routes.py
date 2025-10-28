from fastapi import APIRouter, Depends
from typing import List

from app.models.user import User
from app.schemas.thread import CreateThreadSchema, ThreadResponseSchema
from app.schemas.spark import SparkResponseSchema
from app.services.thread_service import (
    validate_and_create_thread,
    build_thread_response,
    get_user_threads,
    get_thread_by_id_service,
    get_thread_sparks_with_users
)
from app.utils.security import get_current_user

router = APIRouter(tags=["Threads"])


# ---------------------------
# Create Thread
# ---------------------------
@router.post("/", response_model=ThreadResponseSchema)
async def create_thread(
    thread_data: CreateThreadSchema,
    current_user: User = Depends(get_current_user)
):
    thread = await validate_and_create_thread(thread_data, current_user)
    return build_thread_response(thread, current_user, current_user)


# ---------------------------
# Get My Threads
# ---------------------------
@router.get("/me", response_model=List[ThreadResponseSchema])
async def get_my_threads(current_user: User = Depends(get_current_user)):
    return await get_user_threads(current_user)


# ---------------------------
# Get Thread by ID
# ---------------------------
@router.get("/{thread_id}", response_model=ThreadResponseSchema)
async def get_thread_by_id(thread_id: str, current_user: User = Depends(get_current_user)):
    return await get_thread_by_id_service(thread_id, current_user)


# ---------------------------
# Get Ordered Sparks for a Thread
# ---------------------------
@router.get("/{thread_id}/sparks/ordered", response_model=List[SparkResponseSchema])
async def fetch_ordered_sparks(thread_id: str, current_user: User = Depends(get_current_user)):
    return await get_thread_sparks_with_users(thread_id, current_user)



