package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.EmailService;
import com.example.demo.service.OtpService;

@RestController
@RequestMapping("/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    // Send OTP
//    @GetMapping("/send")
//    public String sendOtp(@RequestParam String email) {
//
//        String otp = otpService.generateOtp(email);
//        emailService.sendOtpEmail(email, otp);
//
//        return "OTP sent to " + email;
//    }

    // Verify OTP
    @GetMapping("/verify")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp) {

        boolean valid = otpService.verifyOtp(email, otp);

        return valid ? "OTP Verified ✅" : "Invalid OTP ❌";
    }
}