from fastapi import APIRouter, HTTPException, Depends
from beanie import PydanticObjectId

from app.schemas.chapter import CreateChapterSchema, ChapterResponseSchema
# from app