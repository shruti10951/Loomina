from fastapi import APIRouter, HTTPException, Depends
from beanie import PydanticObjectId


from app.schemas.spark import CreateSparkSchema, SparkResponseSchema
from app.services.spark_service import validate_and_create_spark


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

    return SparkResponseSchema(
        _id=str(spark.id),
        threadId=str(spark.threadId),
        user={
            "_id": str(current_user.id),
            "username": current_user.username,
            "userProfileImage": current_user.userProfileImage,
        },
        sparkText=spark.sparkText,
        creationTime=spark.creationTime,
        numberOfLikes=spark.numberOfLikes,
        numberOfComments=spark.numberOfComments,
        likedBy=spark.likedBy,
        previousSparkId=spark.previousSparkId,
        isStart=spark.isStart,
        isReported=spark.isReported,
        isDeleted=spark.isDeleted,
        isSensitive=spark.isSensitive,
        isEdited=spark.isEdited,
        reportCount=spark.reportCount,
    )



# ---------------------------
# Get Spark by ID
# ---------------------------
@router.get('/{spark_id}', response_model=SparkResponseSchema)
async def get_spark_by_id(spark_id: str, current_user: User = Depends(get_current_user)):
    try:
        spark_obj_id = PydanticObjectId(spark_id)
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid spark ID format")
    
    spark = await Spark.get(spark_obj_id)
    if not spark:
        raise HTTPException(status_code=404, detail="Spark not found")
    
    # Fetch user info for the spark creator
    spark_user = await User.get(spark.userId)
    if not spark_user:
        raise HTTPException(status_code=404, detail="Spark creator not found")
    
    return SparkResponseSchema(
    _id=str(spark.id),
    threadId=str(spark.threadId),
    user={
        "_id": str(spark_user.id),
        "username": spark_user.username,
        "userProfileImage": spark_user.userProfileImage,
    },
    sparkText=spark.sparkText,
    creationTime=spark.creationTime,
    numberOfLikes=spark.numberOfLikes,
    numberOfComments=spark.numberOfComments,
    likedBy=spark.likedBy,
    previousSparkId=spark.previousSparkId,
    isStart=spark.isStart,
    isReported=spark.isReported,
    isDeleted=spark.isDeleted,
    isSensitive=spark.isSensitive,
    isEdited=spark.isEdited,
    reportCount=spark.reportCount,
)