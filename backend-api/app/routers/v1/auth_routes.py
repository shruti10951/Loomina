from fastapi import APIRouter, HTTPException, status
from datetime import timedelta

from app.models.user import User
from app.schemas.auth import LoginSchema, TokenResponse
from app.utils.hashing import verify_password
from app.utils.jwt import create_access_token
from app.core.config import settings

router = APIRouter()

@router.post("/login", response_model=TokenResponse)
async def login(payload: LoginSchema):
    # Find user by email
    user = await User.find_one(User.email == payload.email)
    if not user or not verify_password(payload.password, user.password):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid credentials"
        )

    # Create token with email as subject
    access_token_expires = timedelta(minutes=settings.ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": user.email},
        expires_delta=access_token_expires
    )

    return {"access_token": access_token, "token_type": "bearer"}
