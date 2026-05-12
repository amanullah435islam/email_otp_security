package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.EmailService;
import com.example.demo.service.OtpService;
import com.example.security.JwtUtil;


// //Login Request → OTP Email → OTP Verify → JWT Token → Secure API Access
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    // STEP 1: Send OTP
    @PostMapping("/login")
    public String login(@RequestParam String email) {

        String otp = otpService.generateOtp(email);
        emailService.sendOtp(email, otp);

        return "OTP sent to email";
    }

    // STEP 2: Verify OTP & generate JWT
    @PostMapping("/verify")
    public String verify(@RequestParam String email,
                         @RequestParam String otp) {

        boolean valid = otpService.verifyOtp(email, otp);

        if (valid) {
            return jwtUtil.generateToken(email);
        } else {
            return "Invalid OTP";
        }
    }
}
