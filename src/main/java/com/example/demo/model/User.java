package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import com.example.enum1.Role;
import com.example.passkey.PasskeyCredential;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(
	    name = "app_user",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = "email")
	    }
	)

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    private String name;

    
    @Column(unique = true, nullable = false)
    private String email;

    
//    @Column(nullable = true)
    private String password;

    
    private boolean verified;

    
    private String verificationToken;

    
    @Enumerated(EnumType.STRING)
    private Role role;
    //private String role;
    
    
    //private String Otp;
    
    
    private String resetToken;

    
    private LocalDateTime resetTokenExpiry;
    
    @OneToMany(mappedBy = "user", 
	    		cascade = CascadeType.ALL, 
	    		orphanRemoval = true
	    		)
    @JsonManagedReference
    private List<PasskeyCredential> credentials;
    
    
}