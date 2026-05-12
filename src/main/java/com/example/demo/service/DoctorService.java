package com.example.demo.service;



import com.example.demo.model.Doctor;

import java.util.List;


public interface DoctorService {

	
	Doctor createDoctor(Doctor p);
	
	List<Doctor> getAllDoctor();
	
	Doctor getDoctorById(Long id);
	
	Doctor updateDoctor(Long id, Doctor p);
	
	void deleteDoctor(Long id);
}
