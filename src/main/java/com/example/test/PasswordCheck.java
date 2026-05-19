package com.example.test;

	import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

	public class PasswordCheck {

	    public static void main(String[] args) {

	        BCryptPasswordEncoder encoder =
	                new BCryptPasswordEncoder();

	        String hash =
	                encoder.encode("AmAnUllAh2024");

	        System.out.println(hash);
	    }
	}