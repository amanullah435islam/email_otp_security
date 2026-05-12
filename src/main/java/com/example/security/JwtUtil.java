package com.example.security;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "mySecretKeymySecretKeymySecretKey12345"; 
    // must be at least 256-bit

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Generate Token
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSigningKey())
                .compact();
    }
  
    
    // Extract Email
    public String extractEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
  
// //or::::
    
//    public String extractEmail(String token) {
//        return Jwts.parser()
//                .setSigningKey(SECRET)
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
    
}