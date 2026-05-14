package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;

// //alternative:::

import org.springframework.web.bind.annotation.*;

import com.example.demo.model.AppUser;
import com.example.demo.repo.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.OtpService;
import com.example.security.JwtUtil;
import java.util.Random;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;




@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private EmailService emailService;

    private JwtUtil jwtUtil = new JwtUtil();

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public String register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password
    ) {

        // CHECK EMAIL EXISTS
        AppUser existingUser = repo.findByEmail(email);

        if (existingUser != null) {
            return "Email Already Exists";
        }

        // GENERATE TOKEN
        String token = UUID.randomUUID().toString();

        // CREATE USER
        AppUser user = new AppUser();

        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        user.setVerified(false);

        user.setVerificationToken(token);

        // SAVE USER
        repo.save(user);

        // SEND EMAIL
        emailService.sendVerificationEmail(
                email,
                token
        );

        return "Verification Email Sent Successfully";
    }

    // =========================
    // VERIFY EMAIL
    // =========================
    @GetMapping("/verify")
    public String verifyEmail(
            @RequestParam String token
    ) {

        AppUser user =
                repo.findByVerificationToken(token);

        if (user == null) {
            return "Invalid Verification Token";
        }

        // VERIFY ACCOUNT
        user.setVerified(true);

        // REMOVE TOKEN
        user.setVerificationToken(null);

        repo.save(user);

        return "Account Verified Successfully";
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password
    ) {

        AppUser user = repo.findByEmail(email);

        // USER CHECK
        if (user == null) {
            return "User Not Found";
        }

        // PASSWORD CHECK
        if (!user.getPassword().equals(password)) {
            return "Wrong Password";
        }

        // EMAIL VERIFIED CHECK
        if (!user.isVerified()) {
            return "Please Verify Your Email First";
        }

        // GENERATE JWT
        String jwtToken =
                jwtUtil.generateToken(email);

        return jwtToken;
    }
}





// // // second time auth in token::::::::::::::::


//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    private UserRepository repo;
//
//    @Autowired
//    private EmailService emailService;
//
//    private JwtUtil jwtUtil = new JwtUtil();
//
//    // REGISTER
//    @PostMapping("/register")
//    public String register(@RequestParam String email,
//                           @RequestParam String password) {
//
//        AppUser user = new AppUser();
//
//        String otp =
//                String.valueOf(100000 + new Random().nextInt(900000));
//
//        user.setEmail(email);
//        user.setPassword(password);
//        user.setOtp(otp);
//        user.setVerified(false);
//
//        repo.save(user);
//
//        emailService.sendOtp(email, otp);
//
//        return "OTP Sent to Email";
//    }
//
//    // VERIFY REGISTER OTP
//    @PostMapping("/verify-register")
//    public String verifyRegister(@RequestParam String email,
//                                 @RequestParam String otp) {
//
//        AppUser user = repo.findByEmail(email);
//
//        if (user == null) {
//            return "User Not Found";
//        }
//
//        if (user.getOtp().equals(otp)) {
//
//            user.setVerified(true);
//
//            repo.save(user);
//
//            String token =
//                    jwtUtil.generateToken(email);
//
//            return token;
//        }
//
//        return "Invalid OTP";
//    }
//
//    // LOGIN
//    @PostMapping("/login")
//    public String login(@RequestParam String email,
//                        @RequestParam String password) {
//
//        AppUser user = repo.findByEmail(email);
//
//        if (user == null) {
//            return "User Not Found";
//        }
//
//        if (!user.getPassword().equals(password)) {
//            return "Wrong Password";
//        }
//
//        String otp =
//                String.valueOf(100000 + new Random().nextInt(900000));
//
//        user.setOtp(otp);
//
//        repo.save(user);
//
//        emailService.sendOtp(email, otp);
//
//        return "Login OTP Sent";
//    }
//
//    // VERIFY LOGIN OTP
//    @PostMapping("/verify-login")
//    public String verifyLogin(@RequestParam String email,
//                              @RequestParam String otp) {
//
//        AppUser user = repo.findByEmail(email);
//
//        if (user.getOtp().equals(otp)) {
//
//            String token =
//                    jwtUtil.generateToken(email);
//
//            return token;
//        }
//
//        return "Invalid OTP";
//    }
//}
//









// // second time auth in token::::::::::::::::


//// //Login Request → OTP Email → OTP Verify → JWT Token → Secure API Access
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    private OtpService otpService;
//
//    @Autowired
//    private EmailService emailService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    // STEP 1: Send OTP
//    @PostMapping("/login")
//    public String login(@RequestParam String email) {
//
//        String otp = otpService.generateOtp(email);
//        emailService.sendOtp(email, otp);
//
//        return "OTP sent to email";
//    }
//
//    // STEP 2: Verify OTP & generate JWT
//    @PostMapping("/verify")
//    public String verify(@RequestParam String email,
//                         @RequestParam String otp) {
//
//        boolean valid = otpService.verifyOtp(email, otp);
//
//        if (valid) {
//            return jwtUtil.generateToken(email);
//        } else {
//            return "Invalid OTP";
//        }
//    }
//}
