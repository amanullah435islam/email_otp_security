echo "# email_otp_security" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/amanullah435islam/email_otp_security.git
git push -u origin main
…or push an existing repository from the command line
git remote add origin https://github.com/amanullah435islam/email_otp_security.git
git branch -M main
git push -u origin main







Ekhon ei command dao:

git pull origin main --rebase

Jodi editor open hoy, tahole:

:wq → Vim hole save & exit
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
    ↓
Authentication Complete




