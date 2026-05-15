package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.UserService;
import com.example.dto.ChangePasswordRequest;
import com.example.dto.ProfileResponse;
import com.example.dto.UpdateProfileRequest;
import com.example.response.AuthResponse;

import org.springframework.beans.factory.annotation
.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // PROFILE
    @GetMapping("/profile")
    public ProfileResponse profile() {

        return userService.getProfile();
    }

    // UPDATE PROFILE
    @PutMapping("/update")
    public AuthResponse update(
            @RequestBody
            UpdateProfileRequest request
    ) {

        return userService.updateProfile(
                request
        );
    }

    // CHANGE PASSWORD
    @PutMapping("/change-password")
    public AuthResponse changePassword(
            @RequestBody
            ChangePasswordRequest request
    ) {

        return userService.changePassword(
                request
        );
    }
}





















//@RestController
//@RequestMapping("/user")
//public class UserController {
//
//    @GetMapping("/dashboard")
//    public String dashboard() {
//
//        return "Welcome User";
//    }
//}






//@RestController
//@RequestMapping("/auth")
//public class UserController {
//
//    @GetMapping("/profile")
//    public String profile() {
//
//        return "Welcome Protected API";
//    }
//}