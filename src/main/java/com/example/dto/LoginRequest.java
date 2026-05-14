package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest {


    @Email(message = "Invalid Email")
    @NotBlank(message = "Email Required")
    private String email;

    @NotBlank(message = "Password Required")
    private String password;
}
