package com.example.demo.service;


import com.example.demo.model.Doctor;
import com.example.demo.repo.IDoctorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DoctorServiceImp implements DoctorService{



	private final IDoctorRepo doctorRepo;
	
	
	@Override
	public Doctor createDoctor(Doctor p) {
		
		return doctorRepo.save(p);
	}

	@Override
	public List<Doctor> getAllDoctor() {
		
		return doctorRepo.findAll();
	}

	@Override
	public Doctor updateDoctor(Long id, Doctor p) {

		Doctor existing = doctorRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Doctor not found"));
		
		existing.setDoctorCode(p.getDoctorCode());
		existing.setDoctorName(p.getDoctorName());
		existing.setSpecialize(p.getSpecialize());
		existing.setContact(p.getContact());	
		existing.setAvailability(p.getAvailability());
		existing.setEmail(p.getEmail());
		existing.setRoomNumber(p.getRoomNumber());
		existing.setDescription(p.getDescription());
		existing.setImage(p.getImage());
		
				
		
		return doctorRepo.save(existing);
	}

	@Override
	public void deleteDoctor(Long id) {
		
		doctorRepo.deleteById(id);
		
	}

	@Override
	public Doctor getDoctorById(Long id) {
		
		return doctorRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
	}

}
