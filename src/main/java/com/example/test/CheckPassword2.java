package com.example.test;


	import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

	public class CheckPassword2 {

	    public static void main(String[] args) {

	        BCryptPasswordEncoder encoder =
	                new BCryptPasswordEncoder();

	        //String rawPassword = "123456";
	        String rawPassword = "AmAnUllAh2024";

	        String dbPassword =
	"$2a$10$3faj7l38Hbz0c9cZAAPnqe2qebEGwxaQD7NTVfR8EKcUNM2T5WGj2";

	        boolean matched =
	                encoder.matches(
	                        rawPassword,
	                        dbPassword
	                );

	        System.out.println(matched);
	    }
	}