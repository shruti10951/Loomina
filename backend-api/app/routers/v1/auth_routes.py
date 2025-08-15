from fastapi import APIRouter, HTTPException, status, Depends
from fastapi.security import OAuth2PasswordRequestForm
from datetime import timedelta

from app.models.user import User
from app.schemas.auth import LoginSchema, TokenResponse
from app.utils.hashing import verify_password
from app.utils.jwt import create_access_token
from app.core.config import settings

router = APIRouter()


# -------------------------
# 1. Form login for Swagger
# -------------------------
@router.post("/login", response_model=TokenResponse)
async def login_form(form_data: OAuth2PasswordRequestForm = Depends()):
    """
    Form-data login for Swagger OAuth2 popup.
    Maps `username` field to `email`.
    """
    email = form_data.username
    password = form_data.password

    user = await User.find_one(User.email == email)
    if not user or not verify_password(password, user.password):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid credentials"
        )

    access_token_expires = timedelta(minutes=settings.ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": user.email},
        expires_delta=access_token_expires
    )

    return {"access_token": access_token, "token_type": "bearer"}


# -------------------------
# 2. JSON login for your app
# -------------------------
@router.post("/login/json", response_model=TokenResponse)
async def login_json(payload: LoginSchema):
    """
    JSON login for app/clients.
    """
    user = await User.find_one(User.email == payload.email)
    if not user or not verify_password(payload.password, user.password):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid credentials"
        )

    access_token_expires = timedelta(minutes=settings.ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": user.email},
        expires_delta=access_token_expires
    )

    return {"access_token": access_token, "token_type": "bearer"}
