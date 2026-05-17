package com.example.security;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

//    private static final String SECRET =					//normal
//            "mysecretkeymysecretkeymysecretkey12345";
    
//    private static final String SECRET =					//strong but not better
//            "mysecretkeymysecretkeymysecretkey123456"; 
    
    private static final String SECRET =
            "mySuperSecretKeyForJwtAuthentication2026SecureKey";
    
    // SECRET KEY
    private Key getKey() {

        return Keys.hmacShaKeyFor(
                SECRET.getBytes()
        );
    }

    // GENERATE TOKEN
    public String generateToken(
            String email,
            String role
    ) {

        return Jwts.builder()

                .setSubject(email)

                .claim("role", role)

                .setIssuedAt(new Date())

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60
                        )
                )

                //.signWith(getKey()) 		//generate → signWith  & 	// verify   → setSigningKey
                
                .signWith(getKey(), SignatureAlgorithm.HS256)

                .compact();
    }

    // EXTRACT ALL CLAIMS
    public Claims extractClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getKey())               
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // EXTRACT EMAIL
    public String extractEmail(String token) {

        return extractClaims(token)
                .getSubject();
    }

    // EXTRACT ROLE
    public String extractRole(String token) {

        return extractClaims(token)
                .get("role", String.class);
    }

    // TOKEN EXPIRED CHECK
    public boolean isTokenExpired(String token) {

        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // VALIDATE TOKEN
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    
    
    
    public String generateRefreshToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 604800000)
                )
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}









// //secondtime auth in token:::::::::::::::::::::::


//@Component
//public class JwtUtil {
//
//    private static final String SECRET = "mySecretKeymySecretKeymySecretKey12345"; 
//    // must be at least 256-bit
//
//    private Key getSigningKey() {
//        return Keys.hmacShaKeyFor(SECRET.getBytes());
//    }
//
//    // Generate Token
//    public String generateToken(String email) {
//    	
//        return Jwts.builder()
//                .setSubject(email)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
//                //.setExpiration(new Date(System.currentTimeMillis() + 3600000))
//                .signWith(getSigningKey())
//                .compact();
//    }
//  
//    
//    // Extract Email
//    public String extractEmail(String token) {
//    	
////        Claims claims = Jwts.parserBuilder()
////                .setSigningKey(getSigningKey())
////                .build()
////                .parseClaimsJws(token)
////                .getBody();
////
////        return claims.getSubject();
//    	
//
//        return Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    
//    }
//    
//}