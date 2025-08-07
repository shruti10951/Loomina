
from fastapi import APIRouter, HTTPException, Depends
from fastapi.security import OAuth2PasswordRequestForm, OAuth2PasswordBearer
from app.schemas.auth import LoginSchema, TokenResponse
from app.models.user import User
from app.utils.hashing import verify_password
from app.utils.jwt import create_access_token, decode_access_token
from app.schemas.user import UserResponseSchema
from beanie import PydanticObjectId

router = APIRouter(prefix="/auth", tags=["Authentication"])

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/auth/login")

# ------------------------
# POST /auth/login
# ------------------------
@router.post("/login", response_model=TokenResponse)
async def login_user(credentials: LoginSchema):
    user = await User.find_one(User.email == credentials.email)
    if not user or not verify_password(credentials.password, user.password):
        raise HTTPException(status_code=401, detail="Invalid email or password")
    
    token_data = {"sub": str(user.id)}
    access_token = create_access_token(data=token_data)
    
    return TokenResponse(access_token=access_token)

# ------------------------
# GET /auth/me
# Requires: Bearer token
# ------------------------
@router.get("/me", response_model=UserResponseSchema)
async def get_current_user(token: str = Depends(oauth2_scheme)):
    payload = decode_access_token(token)
    if not payload:
        raise HTTPException(status_code=401, detail="Invalid token")

    user_id = payload.get("sub")
    user = await User.get(PydanticObjectId(user_id))
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    return UserResponseSchema(**user.model_dump(), id=str(user.id))
