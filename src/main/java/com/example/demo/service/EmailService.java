package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(
            String toEmail,
            String token
    ) {

        try {

            String verifyLink =
                    "http://localhost:8080/auth/verify?token="
                            + token;

            SimpleMailMessage message =
                    new SimpleMailMessage();

            message.setTo(toEmail);

            message.setSubject("Verify Your Account");

            message.setText(
                    "Click the link below:\n"
                            + verifyLink
            );

            mailSender.send(message);

            System.out.println("MAIL SENT SUCCESSFULLY");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}


// //secondtime change::::::::::::::::::::::

//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void sendOtp(String email, String otp) {
//
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(email);
//        msg.setSubject("Login OTP");
//        msg.setText("Your OTP is: " + otp);
//
//        mailSender.send(msg);
//    }
//}






//normal email & otp code::::::::::::::::::

//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//// //just mail check:::::::::::
//    public void sendEmail(String toEmail,
//                          String subject,
//                          String body) {
//
//        SimpleMailMessage message = new SimpleMailMessage();
//
////        message.setTo(toEmail);
////        message.setSubject(subject);
////        message.setText(body);
//               
//        message.setTo("amanullah435islam@gmail.com");
//        message.setSubject("Test Mail");
//        message.setText("Hello Spring Boot Mail");
//
//        mailSender.send(message);
//
//        System.out.println("Mail Sent Successfully...");
//    }
//    
//    
//// //otp check:::::::::::::::::::::
//    public void sendOtpEmail(String toEmail, String otp) {
//
//        SimpleMailMessage message = new SimpleMailMessage();
//
//        message.setTo(toEmail);
//        message.setSubject("Your OTP Code");
//        message.setText("Your OTP is: " + otp + " (Valid for few minutes)");
//
//        mailSender.send(message);
//    }
//}