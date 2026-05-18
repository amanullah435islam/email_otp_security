package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.
UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.
SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.
WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.
OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	private static final Logger log =
	        LoggerFactory.getLogger(JwtFilter.class);
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
    	
    	String path = request.getServletPath();

    	if (path.startsWith("/auth")
    			//if (path.startsWith("/auth/passkey")	//just check purpose
    	        || path.startsWith("/oauth2")
    	        || path.startsWith("/login")
    	        || path.startsWith("/api/test/public")
    			) {

    	    filterChain.doFilter(request, response);
    	    return;
    	}

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

     // ✅ validate
     if (!jwtUtil.validateToken(token)) {
         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
         return;
     }

     try {

         Claims claims = jwtUtil.extractClaims(token);
         String email = claims.getSubject();

         // ✅ prevent duplicate auth
         if (SecurityContextHolder.getContext().getAuthentication() == null) {

             UserDetails userDetails =
                     userDetailsService.loadUserByUsername(email);

             UsernamePasswordAuthenticationToken auth =
                     new UsernamePasswordAuthenticationToken(
                             userDetails,
                             null,
                             userDetails.getAuthorities()
                     );

             auth.setDetails(
                     new WebAuthenticationDetailsSource()
                             .buildDetails(request)
             );

             SecurityContextHolder
                     .getContext()
                     .setAuthentication(auth);
         }
         
         //System.out.println("AUTH SET DONE");
         log.info("JWT authenticated for {}", email);
         
     	} catch (Exception e) {
     		
            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }
        
        
        
        filterChain.doFilter(request, response);
    }
}