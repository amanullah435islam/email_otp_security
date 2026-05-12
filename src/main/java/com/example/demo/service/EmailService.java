package com.example.demo.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

// //just mail check:::::::::::
    public void sendEmail(String toEmail,
                          String subject,
                          String body) {

        SimpleMailMessage message = new SimpleMailMessage();

//        message.setTo(toEmail);
//        message.setSubject(subject);
//        message.setText(body);
               
        message.setTo("amanullah435islam@gmail.com");
        message.setSubject("Test Mail");
        message.setText("Hello Spring Boot Mail");

        mailSender.send(message);

        System.out.println("Mail Sent Successfully...");
    }
    
    
// //otp check:::::::::::::::::::::
    public void sendOtpEmail(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + " (Valid for few minutes)");

        mailSender.send(message);
    }
}