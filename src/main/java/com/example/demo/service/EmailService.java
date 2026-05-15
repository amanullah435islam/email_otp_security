package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String token) {

        try {
            String verifyLink =
                    "http://localhost:8080/auth/verify?token=" + token;

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            // Sender email + display name
            helper.setFrom(
                    "yourgmail@gmail.com",
                    "My Application"
            );

            helper.setTo(toEmail);

            helper.setSubject(
                    "Account Verification - My Application"
            );

            helper.setText(
                    "Hello,\n\n" +
                    "Thank you for registering.\n\n" +
                    "Please click the link below to verify your account:\n\n"
                    + verifyLink +
                    "\n\nRegards,\nMy Application Team"
            );

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    public void sendResetPasswordEmail(
            String to,
            String token
    ) {

        String link =
                "http://localhost:8080/auth/reset-password-page?token="
                        + token;

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);

        message.setSubject(
                "Reset Password"
        );

        message.setText(
                "Click this link to reset password:\n"
                        + link
        );

        mailSender.send(message);
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