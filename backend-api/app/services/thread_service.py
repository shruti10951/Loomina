from fastapi import HTTPException
from beanie import PydanticObjectId 

from typing import List

from app.models.spark import Spark
from app.models.thread import Thread


async def get_ordered_sparks(threadId: PydanticObjectId) -> List[Spark]:

    # 1. Ensure thread exists
    thread = await Thread.get(threadId)
    if not thread:
        raise HTTPException(status_code=404, detail="Thread not found")
    
    # 2. Fetch all sparks for this thread
    sparks = await Spark.find(Spark.threadId == threadId).to_list()
    if not sparks:
        return []
    
    # safe timestamp helper (fallback to 0 if missing)
    def _ts(dt):
        try:
            return dt.timestamp()
        except Exception:
            return 0

    # 4. Collect all sparks marked as isStart
    start_candidates = [s for s in sparks if s.isStart]
    if start_candidates:
        # choose most liked; tie-breaker -> earlier creation time
        root = max(start_candidates, key=lambda s: (s.numberOfLikes, -_ts(s.creationTime)))
    else:
        # fallback to sparks that have no previousSparkId (explicit root-like)
        no_prev = [s for s in sparks if not s.previousSparkId]
        if no_prev:
            root = max(no_prev, key=lambda s: (s.numberOfLikes, -_ts(s.creationTime)))
        else:
            # last fallback; pick spark with oldest creationtime
            root = min(sparks, key=lambda s: (_ts(s.creationTime), -s.numberOfLikes))
    
    # 5. Walk forward selecting the best child at each step
    ordered_sparks: List[Spark] = []
    visited = set()
    current = root

    while current and str(current.id) not in visited:
        ordered_sparks.append(current)
        visited.add(str(current.id))

        # children are sparks that have previousSparkId == current.id as string
        children = [s for s in sparks if s.previousSparkId == str(current.id)]
        if not children:
            break

        # pick child with most likes (tie -> earlier creationTime)
        current = max(children, key=lambda s: (s.numberOfLikes, -_ts(s.creationTime)))

    return ordered_sparks

