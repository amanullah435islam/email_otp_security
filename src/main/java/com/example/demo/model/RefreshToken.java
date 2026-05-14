package com.example.demo.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name="refresh_token")

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RefreshToken {

    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private Long id;

    private String token;

    private String email;

    private LocalDateTime expiryDate;

//    public Long getId() {
//        return id;
//    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public LocalDateTime getExpiryDate() {
//        return expiryDate;
//    }
//
//    public void setExpiryDate(
//            LocalDateTime expiryDate
//    ) {
//        this.expiryDate = expiryDate;
//    }
}
