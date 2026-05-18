package com.example.passkey;

import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;

import java.util.Set;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//@Configuration
//public class WebAuthnConfig {
//
//    @Bean
//    public RelyingParty relyingParty(
//            InMemoryCredentialRepository repo
//    ) {
//
//        return RelyingParty.builder()
//
//                .identity(
//                        RelyingPartyIdentity.builder()
//                                .id("localhost")
//                                .name("Demo App")
//                                .build()
//                )
//
//                .credentialRepository(repo)
//
//                .origins(
//                        Set.of("http://localhost:4200")
//                )
//
//                .build();
//    }
//}



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class WebAuthnConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(
                //java.util.List.of("http://localhost:5500", "http://127.0.0.1:5500")
                java.util.List.of("http://localhost:5500", "http://localhost:4200")
        );

        config.setAllowedMethods(
                java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        config.setAllowedHeaders(
                java.util.List.of("*")
        );

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}