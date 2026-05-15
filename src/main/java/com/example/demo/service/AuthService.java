package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

import jakarta.transaction.Transactional;

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
    
    @Autowired
    private AuthenticationManager
            authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

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
                    null,
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
                null,
                null
        );
    }

    // LOGIN
    public AuthResponse login(
            LoginRequest request
    ) {

        try {

            authenticationManager.authenticate(

                    new UsernamePasswordAuthenticationToken(

                            request.getEmail(),

                            request.getPassword()
                    )
            );

        } catch (Exception e) {

                e.printStackTrace();

                if (e instanceof DisabledException) {

                    return new AuthResponse(
                            false,
                            "Please Verify Your Email First",
                            null,
                            null
                    );
                }

                return new AuthResponse(
                        false,
                        "Invalid Email or Password",
                        null,
                        null
                );
            }

        AppUser user =
                repo.findByEmail(
                        request.getEmail()
                );

        String accessToken =
                jwtUtil.generateToken(
                        user.getEmail(),
                        user.getRole()
                );

        String refreshToken =
                jwtUtil.generateRefreshToken(
                        user.getEmail()
                );

        RefreshToken rt =
                new RefreshToken();

        rt.setEmail(user.getEmail());

        rt.setToken(refreshToken);

        rt.setExpiryDate(
                LocalDateTime.now().plusDays(7)
        );

        refreshRepo.save(rt);

        return new AuthResponse(
                true,
                "Login Success",
                accessToken,
                refreshToken
        );
    }
    
    
    // MAIL VERIFICATION
    public AuthResponse verifyEmail(
            String token
    ) {

        AppUser user =
                repo.findByVerificationToken(
                        token
                );

        if (user == null) {

            return new AuthResponse(
                    false,
                    "Invalid Verification Token",
                    null,
                    null
            );
        }

        user.setVerified(true);

        user.setVerificationToken(null);

        repo.save(user);

        return new AuthResponse(
                true,
                "Account Verified Successfully",
                null,
                null
        );
    }
    
    // REFRESH TOKEN
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
                    null,
                    null
            );
        }

        if (tokenData.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            return new AuthResponse(
                    false,
                    "Refresh Token Expired",
                    null,
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
                newAccessToken,
                refreshToken
        );
    }
    
    
    //LOGOUT
    @Transactional
    public AuthResponse logout(
            String refreshToken
    ) {

        RefreshToken tokenData =
                refreshRepo.findByToken(
                        refreshToken
                );

        if (tokenData == null) {

            return new AuthResponse(
                    false,
                    "Refresh Token Not Found",
                    null,
                    null
            );
        }

        refreshRepo.deleteByToken(
                refreshToken
        );

        return new AuthResponse(
                true,
                "Logout Successful",
                null,
                null
        );
    }
}
