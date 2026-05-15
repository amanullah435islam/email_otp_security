echo "# email\_otp\_security" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/amanullah435islam/email\_otp\_security.git
git push -u origin main
…or push an existing repository from the command line
git remote add origin https://github.com/amanullah435islam/email\_otp\_security.git
git branch -M main
git push -u origin main







Ekhon ei command dao:

git pull origin main --rebase

Jodi editor open hoy, tahole:

:wq → Vim hole save \& exit
ba simply close kore dao

Tarpor:

git push -u origin main

⚠️ Jodi error ase:

fatal: refusing to merge unrelated histories

tahole ei command dao:

git pull origin main --allow-unrelated-histories --rebase

then:

git push -u origin main





✅ FLOW Registration & login initial process::::::::
Register/Login
    ↓
Send OTP to Email
    ↓
User enters OTP
    ↓
OTP Verify
    ↓
JWT Token Generate




✅ FLOW Registration \& login initial process::::::::

Register/Login

   ↓

Send OTP to Email

   ↓

User enters OTP

   ↓

OTP Verify

   ↓

JWT Token Generate

   ↓

Registration into auto veryfied link than login than token.
Authentication Complete

















User Registration Form Submit

      ↓

System sends Email Verification Link
      ↓

User opens Gmail

      ↓

Clicks Verify Button/Link

      ↓

Backend verifies token

      ↓

Account Activated / Registration Complete





1️⃣ REGISTER

POST http://localhost:8080/auth/register



Params:



name=aman

email=test@gmail.com

password=123

2️⃣ MAIL যাবে



Mail এ আসবে:



Click the link below to verify your account:



http://localhost:8080/auth/verify?token=abcxyz123



3️⃣ LINK CLICK



Browser এ open হবে:



http://localhost:8080/auth/verify?token=abcxyz123

4️⃣ LOGIN

POST http://localhost:8080/auth/login



Params:



email=test@gmail.com

password=123

5️⃣ JWT TOKEN আসবে

eyJhbGciOiJIUzI1NiJ9....

Registration into auto veryfied link than login than token






3. File stage koro
git add README.md


Ei command dao:

git rebase --continue

Jodi abar editor open hoy:


Esc
:wq
Enter

Tarpor abar:


git rebase --continue

Jotokkhon na bole:


Successfully rebased and updated refs/heads/main


Then final push:

git push -u origin main






Login
   ↓
JWT Token পাওয়া
   ↓
Frontend token save করবে
   ↓
Authorization: Bearer TOKEN
   ↓
JWT Filter validate করবে
   ↓
Protected API access







Register
   ↓
Role Save
   ↓
Login
   ↓
JWT Generate
   ↓
JWT Filter
   ↓
Role Check
   ↓
Protected API Access








Login
   ↓
Access Token (15 min)
Refresh Token (7 days)
   ↓
Access token expired
   ↓
Use Refresh Token
   ↓
New Access Token


LOGIN
  ↓
Access Token + Refresh Token
  ↓
Access Token Expired
  ↓
/auth/refresh
  ↓
New Access Token





Proper DTO + Service Layer Refactor

controller/
service/
repository/
entity/
dto/
response/
security/

🔥 REGISTER
{
  "name": "Aman",
  "email": "aman@gmail.com",
  "password": "123456",
  "role": "ADMIN"
}
🔥 LOGIN
{
  "email": "aman@gmail.com",
  "password": "123456"
}
✅ RESPONSE
{
  "success": true,
  "message": "Login Success",
  "token": "eyJhbGc..."
}





Validation + Global Exception Handler

❌ INVALID REGISTER REQUEST
{
  "name": "",
  "email": "abc",
  "password": "12",
  "role": ""
}
✅ RESPONSE
{
  "timestamp": "2026-05-15T00:00:00",
  "status": 400,
  "name": "Name is required",
  "email": "Invalid email format",
  "password": "Password minimum 6 characters",
  "role": "Role is required"
}




LOGIN
POST /auth/login

Response:

Access Token
Refresh Token
🔥 LOGOUT
POST /auth/logout?refreshToken=eyJhbGc...
✅ RESPONSE
{
  "success": true,
  "message": "Logout Successful",
  "token": null
}
🔥 NOW TRY REFRESH
POST /auth/refresh
❌ RESULT
{
  "success": false,
  "message": "Invalid Refresh Token",
  "token": null
}

NOW YOUR AUTH SYSTEM HAS

✔ Registration
✔ Email Verification
✔ BCrypt Password Encryption
✔ JWT Authentication
✔ Refresh Token System
✔ Logout
✔ Refresh Token Revoke
✔ Role Based Security
✔ Validation
✔ Global Exception Handling
✔ Production-Level Architecture






// //Final version::::::::::::::::::::::::::::

🔥 TOKEN TYPES SUMMARY
1. Verification Token
   → Email verify করার জন্য

2. Access Token
   → Protected API access

3. Refresh Token
   → New access token generate

register-> mail verify link click /or verify(postman-DB token)->login->user uses permission/dashboard(accessToken)->logout(refreshToken) ->refresh token/new access token(refreshToken)

1. Register
      ↓
2. Verification Email Sent
      ↓
3. User clicks verification link
      ↓
4. Account Verified
      ↓
5. Login
      ↓
6. Access Token + Refresh Token
      ↓
7. Access Protected APIs
      ↓
8. Access Token Expires
      ↓
9. Refresh Token used
      ↓
10. New Access Token
      ↓
11. Logout
      ↓
12. Refresh Token Revoked/Deleted








Login
  ↓
JWT Token
  ↓
Authorization: Bearer TOKEN
  ↓
Current User Identify
  ↓
Profile Show/Update

🔥 LOGIN
POST /auth/login

Copy JWT token.

🔥 GET PROFILE
GET /user/profile

Header:

Authorization: Bearer TOKEN
✅ RESPONSE
{
  "name": "Aman",
  "email": "aman@gmail.com",
  "role": "ADMIN"
}
🔥 UPDATE PROFILE
PUT /user/update
{
  "name": "Md Amanullah"
}
🔥 CHANGE PASSWORD
PUT /user/change-password
{
  "oldPassword": "123456",
  "newPassword": "new123456"
}







