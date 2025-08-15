from fastapi import Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer
from app.utils.jwt import decode_access_token
from app.models.user import User

# Keep relative path here for Swagger UI compatibility
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/api/v1/auth/login")

async def get_current_user(token: str = Depends(oauth2_scheme)):
    payload = decode_access_token(token)
    if not payload or "sub" not in payload:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid authentication credentials",
            headers={"WWW-Authenticate": "Bearer"},
        )

    # Now we search by email (stored in sub)
    user = await User.find_one(User.email == payload["sub"])
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    return user
