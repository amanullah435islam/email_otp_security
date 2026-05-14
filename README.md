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




<<<<<<< HEAD
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
    ↓
=======



✅ FLOW Registration \& login initial process::::::::

Register/Login

&#x20;   ↓

Send OTP to Email

&#x20;   ↓

User enters OTP

&#x20;   ↓

OTP Verify

&#x20;   ↓

JWT Token Generate

&#x20;   ↓

>>>>>>> 0efb443 (Registration into auto veryfied link than login than token)
Authentication Complete




<<<<<<< HEAD
=======













User Registration Form Submit

&#x20;       ↓

System sends Email Verification Link

&#x20;       ↓

User opens Gmail

&#x20;       ↓

Clicks Verify Button/Link

&#x20;       ↓

Backend verifies token

&#x20;       ↓

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

>>>>>>> 0efb443 (Registration into auto veryfied link than login than token)
