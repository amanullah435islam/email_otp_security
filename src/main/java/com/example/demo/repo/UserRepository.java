package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//User findByEmail(String email);
	Optional<User> findByEmail(String email);

//User findByVerificationToken(String token);
	Optional<User> findByVerificationToken(String token);

//	User findByResetToken(
//	        String resetToken
//	);
	
	Optional<User> findByResetToken(String token);
}