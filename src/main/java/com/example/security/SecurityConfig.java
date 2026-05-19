package com.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;


@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;
    
    @Autowired
    private CustomUserDetailsService
            userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
        .cors(cors -> {})   // 🔥 MUST ADD
        .csrf(csrf -> csrf.disable())
        .authenticationProvider(authenticationProvider()) // 🔥 ADD THIS

                .authorizeHttpRequests(auth -> auth

                        //.requestMatchers("/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/auth/**").permitAll() // 🔥 ADD THIS

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .requestMatchers("/doctor/**").hasRole("DOCTOR")

                        .requestMatchers("/user/**").hasRole("USER")
                        
                        
                        
                        // //permission for test
                        // 🔓 Test public API (optional)
                        .requestMatchers("/api/test/public").permitAll()

                        // 🔐 Role based APIs
                        .requestMatchers("/api/test/admin").hasRole("ADMIN")
                        .requestMatchers("/api/test/doctor").hasRole("DOCTOR")
                        .requestMatchers("/api/test/user").hasRole("USER")

                        .anyRequest().authenticated()
                )
                
//             // //ai oauth2Login Google login ar jonno::::::::::::::
//                .oauth2Login(oauth -> oauth
//
//                        .defaultSuccessUrl(
//                                "/auth/google-success",
//                                true
//                        )
//                     )
                
                // //ai session management rakle Google login null ase::::::::::::
                .sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
                // 🔥 THIS IS MISSING IN YOUR PROJECT
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationProvider 
    authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();

        provider.setUserDetailsService(
                userDetailsService
        );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }
    
    
    @Bean
    public AuthenticationManager
    authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }
    
}














// //alternative way:::::::::::::::

//http
//.cors(cors -> cors.configurationSource(request -> {
//    var config = new org.springframework.web.cors.CorsConfiguration();
//    config.addAllowedOrigin("http://localhost:4200");
//    config.addAllowedMethod("*");
//    config.addAllowedHeader("*");
//    config.setAllowCredentials(true);
//    return config;
//}))
//.csrf(csrf -> csrf.disable())
//.authenticationProvider(authenticationProvider())
//.authorizeHttpRequests(auth -> auth
//
//    .requestMatchers("/auth/**").permitAll()
//    .requestMatchers("/oauth2/**").permitAll()
//
//    .requestMatchers("/admin/**").hasRole("ADMIN")
//    .requestMatchers("/doctor/**").hasRole("DOCTOR")
//    .requestMatchers("/user/**").hasRole("USER")
//
//    .anyRequest().authenticated()
//)
//.sessionManagement(sess ->
//    sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//)
//.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);