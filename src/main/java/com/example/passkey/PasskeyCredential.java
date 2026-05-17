package com.example.passkey;



import java.time.LocalDateTime;

import com.example.demo.model.AppUser;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="passkey_credential")

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PasskeyCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Lob
    private byte[] credentialId;

    @Lob
    private byte[] publicKey;

    private long signatureCount;

}
