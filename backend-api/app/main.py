from fastapi import FastAPI
from app.routers.v1 import user_routes
from app.routers.v1 import auth_routes

from app.core.init_db import init_db

app = FastAPI()

@app.on_event("startup")
async def on_startup():
    await init_db()

@app.get("/ping")
async def ping():
    return {"msg": "pong"}

# Include routers with prefixes and tags for clarity in docs and URLs
app.include_router(user_routes.router, prefix="/api/v1/users", tags=["Users"])
app.include_router(auth_routes.router, prefix="/api/v1/auth", tags=["Auth"])
