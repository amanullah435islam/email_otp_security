package com.example.passkey;



import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class RelyingPartyConfig {

    @Bean
    public RelyingParty relyingParty(InMemoryCredentialRepository repo) {

        RelyingPartyIdentity rpIdentity =
                RelyingPartyIdentity.builder()
                        .id("localhost")   // MUST match domain
                        .name("Demo App")
                        .build();

        return RelyingParty.builder()
                .identity(rpIdentity)
                .credentialRepository(repo)
                .origins(Set.of(
                        "http://localhost:5500"
                ))
                .build();
    }
    
   
}