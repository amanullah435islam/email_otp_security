package com.example.security;


import org.springframework.beans.factory.annotation
.Autowired;

import org.springframework.security.core.userdetails
.UserDetails;

import org.springframework.security.core.userdetails
.UserDetailsService;

import org.springframework.security.core.userdetails
.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(
            String email
    ) throws UsernameNotFoundException {

        User user =
                repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new CustomUserDetails(user);
    }
}
