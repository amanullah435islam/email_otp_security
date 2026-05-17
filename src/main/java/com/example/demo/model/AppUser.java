package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name="app_user")

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppUser {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

//    @Column(unique = true, nullable = false)
//    private String email;
    private String email;

//    @Column(nullable = true)
//    private String password;
    private String password;

    private boolean verified;

    private String verificationToken;

//    @Enumerated(EnumType.STRING)
//    private Role role;
    private String role;
    
    //private String Otp;
    
    private String resetToken;

    private LocalDateTime resetTokenExpiry;
    
    
}