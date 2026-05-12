package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.EmailService;

@RestController
@RequestMapping("/mail")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send")
    public String sendMail() {

        emailService.sendEmail(
                "receiver@gmail.com",
                "Spring Boot Mail",
                "Hello! Mail Sent Successfully."
        );

        return "Mail Sent Successfully";
    }
    
    
}