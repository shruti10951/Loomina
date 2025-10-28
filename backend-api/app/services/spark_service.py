from fastapi import HTTPException
from beanie import PydanticObjectId 

from typing import List, Optional

from app.models.spark import Spark
from app.models.user import User
from app.models.thread import Thread

from app.schemas.spark import SparkLikeResponseSchema, SparkResponseSchema

# ---------------------------
# Create Spark
# ---------------------------
async def validate_and_create_spark(
        threadId: str,
        userId: PydanticObjectId,
        sparkText: str,
        previousSparkId: Optional[str] = None,
        isStart: bool = False,
        isSensitive: bool = False
) -> Spark :
    
    # 1. Ensure thread exists
    # convert string threadId to objectId
    try:
        thread_id = PydanticObjectId(threadId)
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid thread ID")
    
    thread = await Thread.get(thread_id)
    if not thread:
        raise HTTPException(status_code=404, detail="Thread not found")
    
    # 2. Text validation
    if not sparkText.strip():
        raise HTTPException(status_code=400, detail="Spark text cannot be empty")
    
    if len(sparkText) > 400:
        raise HTTPException(status_code=400, detail="Spark text exceeds 400 characters")
    
    # 3. Previous spark validation
    if previousSparkId:
        prev_spark = await Spark.get(PydanticObjectId(previousSparkId))

        if not prev_spark: 
            raise HTTPException(status_code=404, detail="Previous spark not found")
        
        if prev_spark.threadId != thread_id:
            raise HTTPException(status_code=400, detail="Previous spark does not belong to this thread")
        
    
    # 4. isStart validation
    if isStart and previousSparkId is not None:
        raise HTTPException(status_code=400, detail="Starting spark cannot have a previousSparkId")
    
    # 5. Must be start or continuation
    if not isStart and previousSparkId is None:
        raise HTTPException(status_code=400, detail="Spark must either be start or continuation")
    
    # Every shit is okeee! lets gooooo!!!
    
    # Create and insert
    spark = Spark(
        threadId=thread_id,
        userId=userId,
        sparkText=sparkText.strip(),
        previousSparkId=previousSparkId,
        isStart=isStart,
        isSensitive=isSensitive
    )
    await spark.insert()
    return spark


# ---------------------------
# Build Response Helper
# ---------------------------
async def build_spark_response(spark: Spark, user: User, current_user: Optional[User] = None) -> SparkResponseSchema:
    liked_by_current_user = (
        str(current_user.id) in spark.likedBy if current_user else False
    )

    return SparkResponseSchema(
        _id=str(spark.id),
        threadId=str(spark.threadId),
        user={
            "_id": str(user.id),
            "username": user.username,
            "userProfileImage": user.userProfileImage
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
        likedByCurrentUser=liked_by_current_user
    )

# ---------------------------
# Get Spark by ID
# ---------------------------
async def get_spark_by_id_service(spark_id: str, current_user: User) -> SparkResponseSchema:
    try:
        spark_obj_id = PydanticObjectId(spark_id)
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid spark ID format")
    
    spark = await Spark.get(spark_obj_id)
    if not spark:
        raise HTTPException(status_code=404, detail="Spark not found")
    
    spark_user = await User.get(spark.userId)
    if not spark_user:
        raise HTTPException(status_code=404, detail="Spark creator not found")
    
    return await build_spark_response(spark, spark_user, current_user)



# ---------------------------
# Toggle Like Spark
# ---------------------------
async def toggle_like_spark(
        spark_id: str,
        user: User
) -> SparkLikeResponseSchema:
    try:
        spark_obj_id = PydanticObjectId(spark_id)
    except Exception:
        raise HTTPException(status_code=400, detail="Invalid spark ID format")
    
    spark = await Spark.get(spark_obj_id)
    if not spark:
        raise HTTPException(status_code=404, detail="Spark not found")
    
    user_id_str = str(user.id)

    if user_id_str in spark.likedBy:
        spark.likedBy.remove(user_id_str)
        spark.numberOfLikes = max(spark.numberOfLikes - 1, 0)
        action = "unliked"

    else:
        spark.likedBy.append(user_id_str)
        spark.numberOfLikes += 1
        action = "liked"

    await spark.save()

    liked_by_current_user = user_id_str in spark.likedBy

    return SparkLikeResponseSchema(
        sparkId=spark_id,
        action=action,
        numberOfLikes=spark.numberOfLikes,
        likedByCurrentUser=liked_by_current_user
    )


