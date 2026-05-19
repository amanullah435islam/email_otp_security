package com.example.demo.service;


import org.springframework.beans.factory.annotation
.Autowired;

import org.springframework.security.core.context
.SecurityContextHolder;

import org.springframework.security.crypto.password
.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;
import com.example.dto.ChangePasswordRequest;
import com.example.dto.ProfileResponse;
import com.example.dto.UpdateProfileRequest;
import com.example.response.AuthResponse;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // CURRENT USER
    private User getCurrentUser() {

        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String email = auth.getName();

        return repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    // GET PROFILE
    public ProfileResponse getProfile() {

        User user =
                getCurrentUser();

        return new ProfileResponse(
                user.getName(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().name() : "USER"
                
        );
    }

    // UPDATE PROFILE
    public AuthResponse updateProfile(
            UpdateProfileRequest request
    ) {

        User user =
                getCurrentUser();

        user.setName(request.getName());

        repo.save(user);

        return new AuthResponse(
                true,
                "Profile Updated",
                null,
                null
        );
    }

    // CHANGE PASSWORD
    public AuthResponse changePassword(
            ChangePasswordRequest request
    ) {

        User user =
                getCurrentUser();

        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword()
        )) {

            return new AuthResponse(
                    false,
                    "Old Password Incorrect",
                    null,
                    null
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        repo.save(user);

        return new AuthResponse(
                true,
                "Password Changed Successfully",
                null,
                null
        );
    }
}