from fastapi import APIRouter, HTTPException, Depends
from beanie import PydanticObjectId


from app.schemas.spark import CreateSparkSchema, SparkResponseSchema, SparkLikeResponseSchema
from app.services.spark_service import (
    validate_and_create_spark,
    build_spark_response,
    get_spark_by_id_service,
    toggle_like_spark
)


from app.models.spark import Spark
from app.models.user import User

from app.utils.security import get_current_user

# Initialize API Router
router = APIRouter()

# ---------------------------
# Create Spark
# ---------------------------
@router.post("/", response_model=SparkResponseSchema)
async def create_spark(
    spark_data: CreateSparkSchema,
    current_user: User = Depends(get_current_user)
):

    spark = await validate_and_create_spark(
        threadId=spark_data.threadId,
        userId=current_user.id,
        sparkText=spark_data.sparkText,
        previousSparkId=spark_data.previousSparkId,
        isStart=spark_data.isStart or False,
        isSensitive=spark_data.isSensitive or False
    )

    return await build_spark_response(spark, current_user)



# ---------------------------
# Get Spark by ID
# ---------------------------
@router.get('/{spark_id}', response_model=SparkResponseSchema)
async def get_spark_by_id(spark_id: str, current_user: User = Depends(get_current_user)):
    return await get_spark_by_id_service(spark_id, current_user)


@router.post('/{spark_id}/like', response_model=SparkLikeResponseSchema)
async def like_spark(spark_id: str, current_user: User = Depends(get_current_user)):
    return await toggle_like_spark(spark_id, current_user)

