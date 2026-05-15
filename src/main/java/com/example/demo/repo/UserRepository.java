package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.AppUser;


@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

AppUser findByEmail(String email);

AppUser findByVerificationToken(String token);


	AppUser findByResetToken(
	        String resetToken
	);
}