from beanie import init_beanie
from motor.motor_asyncio import AsyncIOMotorClient
from app.models.user import User  # adjust path if needed
from app.core.config import settings  # we'll use this below

async def init_db():
    client = AsyncIOMotorClient(settings.MONGODB_URL)
    db = client.get_default_database()
    await init_beanie(database=db, document_models=[User])
