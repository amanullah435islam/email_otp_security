package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;

// //alternative:::

import org.springframework.web.bind.annotation.*;

import com.example.demo.model.AppUser;
import com.example.demo.model.RefreshToken;
import com.example.demo.repo.RefreshTokenRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.EmailService;
import com.example.demo.service.OtpService;
import com.example.dto.ForgotPasswordRequest;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.dto.ResetPasswordRequest;
import com.example.response.AuthResponse;
import com.example.security.JwtUtil;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public AuthResponse register(
            @Valid
            @RequestBody RegisterRequest request
    ) {

        return authService.register(request);
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public AuthResponse login(
    		@Valid
            @RequestBody LoginRequest request
    ) {

        return authService.login(request);
    }
    
    // =========================
    // VERIFY EMAIL
    // =========================

    @GetMapping("/verify")
    public AuthResponse verifyEmail(
            @RequestParam String token
    ) {

        return authService.verifyEmail(token);
    }
    
    
    // =========================
    // REFRESH TOKEN
    // =========================
    @PostMapping("/refresh")
    public AuthResponse refreshToken(
            @RequestParam String refreshToken
    ) {

        return authService.refreshToken(
                refreshToken
        );
    }
    
    
    // =========================
    // LOGOUT
    // =========================
    @PostMapping("/logout")
    public AuthResponse logout(
            @RequestParam String refreshToken
    ) {

        return authService.logout(
                refreshToken
        );
    }
    
    
    // =========================
    // FORGOT PASSWORD
    // =========================
    @PostMapping("/forgot-password")
    public AuthResponse forgotPassword(
            @RequestBody
            ForgotPasswordRequest request
    ) {

        return authService
                .forgotPassword(request);
    }
    
    
    
    // =========================
    // RESET PASSWORD
    // =========================
    @PostMapping("/reset-password")
    public AuthResponse resetPassword(
            @RequestBody
            ResetPasswordRequest request
    ) {

        return authService
                .resetPassword(request);
    }
    
}























// //Password Encryption + Refresh Token System:::::::::::::::::::::::::::::::::::

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
//    
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    
//    @Autowired
//    private RefreshTokenRepository refreshRepo;
//    
//    
//    // =========================
//    // REGISTER
//    // =========================
//    @PostMapping("/register")
//    public String register(
//            @RequestParam String name,
//            @RequestParam String email,
//            @RequestParam String password,
//            @RequestParam String role
//    ) {
//
//        AppUser existing =
//                repo.findByEmail(email);
//
//        if (existing != null) {
//            return "Email Already Exists";
//        }
//
//        String token =
//                UUID.randomUUID().toString();
//
//        AppUser user = new AppUser();
//
//        user.setName(name);
//        user.setEmail(email);
//        user.setPassword(
//                passwordEncoder.encode(password)
//        );
//
//        //user.setRole(role);
//        user.setRole(role.toUpperCase());
//
//        user.setVerified(false);
//
//        user.setVerificationToken(token);
//
//        repo.save(user);
//
//        emailService.sendVerificationEmail(
//                email,
//                token
//        );
//
//        return "Verification Email Sent";
//    }
//
//    // =========================
//    // VERIFY EMAIL
//    // =========================
//    @GetMapping("/verify")
//    public String verifyEmail(
//            @RequestParam String token
//    ) {
//
//        AppUser user =
//                repo.findByVerificationToken(token);
//
//        if (user == null) {
//            return "Invalid Verification Token";
//        }
//
//        // VERIFY ACCOUNT
//        user.setVerified(true);
//
//        // REMOVE TOKEN
//        user.setVerificationToken(null);
//
//        repo.save(user);
//
//        return "Account Verified Successfully";
//    }
//
//    // =========================
//    // LOGIN
//    // =========================
//    @PostMapping("/login")
//    public String login(
//            @RequestParam String email,
//            @RequestParam String password
//    ) {
//
//        AppUser user =
//                repo.findByEmail(email);
//
//        if (user == null) {
//            return "User Not Found";
//        }
//        
//        if (!passwordEncoder.matches(
//                password,
//                user.getPassword()
//        )) {
//
//            return "Wrong Password";
//        }
//
//        if (!user.isVerified()) {
//
//            return "Please Verify Email First";
//        }
//        
//        
//        String accessToken =
//                jwtUtil.generateToken(
//                        user.getEmail(),
//                        user.getRole()
//                );
//
//        String refreshToken =
//                jwtUtil.generateRefreshToken(
//                        user.getEmail()
//                );
//
//        RefreshToken rt =
//                new RefreshToken();
//
//        rt.setEmail(user.getEmail());
//
//        rt.setToken(refreshToken);
//
//        rt.setExpiryDate(
//                LocalDateTime.now().plusDays(7)
//        );
//
//        refreshRepo.save(rt);
//
//        return
//                "ACCESS TOKEN:\n"
//                        + accessToken
//                        + "\n\nREFRESH TOKEN:\n"
//                        + refreshToken;
//    }
//    
//   
//    // =========================
//    // REFRESH
//    // =========================
//    @PostMapping("/refresh")
//    public String refreshToken(
//            @RequestParam String refreshToken
//    ) {
//
//        RefreshToken tokenData =
//                refreshRepo.findByToken(
//                        refreshToken
//                );
//
//        if (tokenData == null) {
//
//            return "Invalid Refresh Token";
//        }
//
//        if (tokenData.getExpiryDate()
//                .isBefore(LocalDateTime.now())) {
//
//            return "Refresh Token Expired";
//        }
//
//        AppUser user =
//                repo.findByEmail(
//                        tokenData.getEmail()
//                );
//
//        return jwtUtil.generateToken(
//                user.getEmail(),
//                user.getRole()
//        );
//    }
//    
//  
//}





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
