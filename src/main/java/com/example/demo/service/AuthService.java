package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.model.RefreshToken;
import com.example.demo.repo.RefreshTokenRepository;
import com.example.demo.repo.UserRepository;
import com.example.dto.ForgotPasswordRequest;
import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.dto.ResetPasswordRequest;
import com.example.enum1.Role;
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

    	repo.findByEmail(request.getEmail()).ifPresent(user -> {
    	    throw new RuntimeException("Email Already Exists");
    	});
    	
        String token =
                UUID.randomUUID().toString();

        User user =
                new User();

        user.setName(request.getName());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        //user.setRole(request.getRole());
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));

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

        User existing = repo.findByEmail(request.getEmail())
        			.orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!existing.isVerified()) {
            return new AuthResponse(false, "Please verify email first", null, null);
        }
        
        
        String accessToken =
                jwtUtil.generateToken(
                		existing.getEmail(),
                		existing.getRole()
                );

        String refreshToken =
                jwtUtil.generateRefreshToken(
                		existing.getEmail()
                );

        RefreshToken rt =
                new RefreshToken();

        rt.setEmail(existing.getEmail());

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

        User user =
                repo.findByVerificationToken(
                        token
                ) 
                .orElseThrow(() -> new RuntimeException("Invalid Verification Token"));

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

        User user = repo.findByEmail(tokenData.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

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
    
    
    // FORGOT PASSWORD METHOD
    public AuthResponse forgotPassword(
            ForgotPasswordRequest request
    ) {

    	User user = repo.findByEmail(request.getEmail())
    			 .orElseThrow(() -> new RuntimeException("User not found"));    	

        String token =
                UUID.randomUUID()
                        .toString();

        user.setResetToken(token);

        user.setResetTokenExpiry(
                LocalDateTime.now()
                        .plusMinutes(15)
        );

        repo.save(user);

        emailService.sendResetPasswordEmail(
                user.getEmail(),
                token
        );

        return new AuthResponse(
                true,
                "Reset Password Email Sent",
                null,
                null
        );
    }
    
    
    // RESET PASSWORD METHOD
    public AuthResponse resetPassword(
            ResetPasswordRequest request
    ) {

        User user =
                repo.findByResetToken(
                        request.getToken()
                ) 
                .orElseThrow(() -> new RuntimeException("Invalid Token"));

   
        if (user.getResetTokenExpiry() == null ||
        	    user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {

            return new AuthResponse(
                    false,
                    "Token Expired",
                    null,
                    null
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        user.setResetToken(null);

        user.setResetTokenExpiry(null);

        repo.save(user);

        return new AuthResponse(
                true,
                "Password Reset Successful",
                null,
                null
        );
    }
    
    
    // GOOGLE LOGIN
    public AuthResponse googleLoginSuccess(
            OAuth2AuthenticationToken auth
    ) {

    	   // NULL CHECK
        if (auth == null) {

            return new AuthResponse(
                    false,
                    "Google Authentication Failed",
                    null,
                    null
            );
        }
        
        String email =
                auth.getPrincipal()
                        .getAttribute("email");

        String name =
                auth.getPrincipal()
                        .getAttribute("name");

        User user = repo.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setName(name);
                    newUser.setEmail(email);
                   // newUser.setPassword("GOOGLE_LOGIN");
                    newUser.setPassword(passwordEncoder.encode("GOOGLE_LOGIN"));
                    newUser.setRole(Role.USER);               
                    newUser.setVerified(true);
                    return repo.save(newUser);
                });

        // ACCESS TOKEN
        String accessToken =
                jwtUtil.generateToken(
                        user.getEmail(),
                        user.getRole()
                );

        // REFRESH TOKEN
        String refreshToken =
                jwtUtil.generateRefreshToken(
                        user.getEmail()
                );

        // SAVE REFRESH TOKEN
        RefreshToken rt = new RefreshToken();

        rt.setEmail(user.getEmail());

        rt.setToken(refreshToken);

        rt.setExpiryDate(
                LocalDateTime.now().plusDays(7)
        );

        refreshRepo.save(rt);

        // RESPONSE
        return new AuthResponse(
                true,
                "Google Login Success",
                accessToken,
                refreshToken
        );
    }
    
}
