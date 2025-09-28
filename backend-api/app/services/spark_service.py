from fastapi import HTTPException
from beanie import PydanticObjectId 
from app.models.spark import Spark
from app.models.thread import Thread

async def validate_and_create_spark(
        threadId: PydanticObjectId,
        userId: PydanticObjectId,
        sparkText: str,
        previousSparkId: str = None,
        isStart: bool = False,
        isSensitive: bool = False
) -> Spark :
    
    print(previousSparkId)
    print(isStart)
    
    # 1. Ensure thread exists
    thread = await Thread.get(threadId)
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
        
        if prev_spark.threadId != threadId:
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
        threadId=threadId,
        userId=userId,
        sparkText=sparkText.strip(),
        previousSparkId=previousSparkId,
        isStart=isStart,
        isSensitive=isSensitive
    )
    await spark.insert()
    return spark
