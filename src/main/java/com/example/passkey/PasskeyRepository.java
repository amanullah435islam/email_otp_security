package com.example.passkey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasskeyRepository
        extends JpaRepository<PasskeyCredential, Long> {

    List<PasskeyCredential> findByEmail(String email);
}