package com.example.demo.repo;

import com.example.demo.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IDoctorRepo extends JpaRepository<Doctor, Long> {
	//Doctor getDoctorById(Long id);
	Optional<Doctor> findById(Long id);
}
