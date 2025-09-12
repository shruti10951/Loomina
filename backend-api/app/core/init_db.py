from beanie import init_beanie
from motor.motor_asyncio import AsyncIOMotorClient
from app.models.user import User 
from app.models.thread import Thread
from app.core.config import settings 


async def init_db():
    client = AsyncIOMotorClient(settings.MONGODB_URL)
    db = client.get_default_database()
    await init_beanie(database=db, document_models=[User, Thread])
