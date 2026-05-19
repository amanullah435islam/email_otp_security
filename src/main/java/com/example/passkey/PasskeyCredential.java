package com.example.passkey;

import com.example.demo.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "passkey_credential",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "credentialIdBase64")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PasskeyCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

   // private String email;

//    @Lob
//    @Column(length = 1000)
//    private byte[] credentialId;

    
//    @Lob
//    @Column(length = 1000)
//    private byte[] publicKey;
    
    @Column(length = 2000)
    private String credentialIdBase64;
    
    @Column(length = 5000)
    private String publicKey;

    private long signatureCount;
}
