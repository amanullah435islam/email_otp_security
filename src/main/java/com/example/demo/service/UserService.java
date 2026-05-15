package com.example.demo.service;


import org.springframework.beans.factory.annotation
.Autowired;

import org.springframework.security.core.context
.SecurityContextHolder;

import org.springframework.security.crypto.password
.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.example.demo.model.AppUser;
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
    private AppUser getCurrentUser() {

        String email =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return repo.findByEmail(email);
    }

    // GET PROFILE
    public ProfileResponse getProfile() {

        AppUser user =
                getCurrentUser();

        return new ProfileResponse(
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    // UPDATE PROFILE
    public AuthResponse updateProfile(
            UpdateProfileRequest request
    ) {

        AppUser user =
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

        AppUser user =
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