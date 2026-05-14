package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.AppUser;
import com.example.demo.model.RefreshToken;
import com.example.demo.repo.RefreshTokenRepository;
import com.example.demo.repo.UserRepository;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.response.AuthResponse;
import com.example.security.JwtUtil;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private RefreshTokenRepository refreshRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private JwtUtil jwtUtil =
            new JwtUtil();

    // REGISTER
    public AuthResponse register(
            RegisterRequest request
    ) {

        AppUser existing =
                repo.findByEmail(
                        request.getEmail()
                );

        if (existing != null) {

            return new AuthResponse(
                    false,
                    "Email Already Exists",
                    null
            );
        }

        String token =
                UUID.randomUUID().toString();

        AppUser user =
                new AppUser();

        user.setName(request.getName());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(request.getRole());

        user.setVerified(false);

        user.setVerificationToken(token);

        repo.save(user);

        emailService.sendVerificationEmail(
                request.getEmail(),
                token
        );

        return new AuthResponse(
                true,
                "Verification Email Sent",
                null
        );
    }

    // LOGIN
    public AuthResponse login(
            LoginRequest request
    ) {

        AppUser user =
                repo.findByEmail(
                        request.getEmail()
                );

        if (user == null) {

            return new AuthResponse(
                    false,
                    "User Not Found",
                    null
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            return new AuthResponse(
                    false,
                    "Wrong Password",
                    null
            );
        }

        if (!user.isVerified()) {

            return new AuthResponse(
                    false,
                    "Please Verify Email First",
                    null
            );
        }

        String token =
                jwtUtil.generateToken(
                        user.getEmail(),
                        user.getRole()
                );

        return new AuthResponse(
                true,
                "Login Success",
                token
        );
    }
    
    
    public String verifyEmail(
            String token
    ) {

        AppUser user =
                repo.findByVerificationToken(
                        token
                );

        if (user == null) {

            return "Invalid Verification Token";
        }

        user.setVerified(true);

        user.setVerificationToken(null);

        repo.save(user);

        return "Account Verified Successfully";
    }
    
    
    public AuthResponse refreshToken(
            String refreshToken
    ) {

        RefreshToken tokenData =
                refreshRepo.findByToken(
                        refreshToken
                );

        if (tokenData == null) {

            return new AuthResponse(
                    false,
                    "Invalid Refresh Token",
                    null
            );
        }

        if (tokenData.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            return new AuthResponse(
                    false,
                    "Refresh Token Expired",
                    null
            );
        }

        AppUser user =
                repo.findByEmail(
                        tokenData.getEmail()
                );

        String newAccessToken =
                jwtUtil.generateToken(
                        user.getEmail(),
                        user.getRole()
                );

        return new AuthResponse(
                true,
                "New Access Token Generated",
                newAccessToken
        );
    }
}
