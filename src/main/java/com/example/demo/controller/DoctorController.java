package com.example.demo.controller;


import com.example.demo.model.Doctor;
import com.example.demo.service.DoctorServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/doctor")

//@RequiredArgsConstructor
public class DoctorController {

	@Autowired
	private DoctorServiceImp service;
	
	
	@PostMapping("/save")
	public Doctor save(@RequestBody Doctor doctor) {
		return service.createDoctor(doctor);
		
	}
	
	@GetMapping("/getAll")
	public List<Doctor> get(){
		
		return service.getAllDoctor();
		
	}
	
	@GetMapping("/{id}")
	    public Doctor getById(@PathVariable Long id){
	        return service.getDoctorById(id);
	    }

    @PutMapping("/{id}")
    public Doctor update(@PathVariable Long id, @RequestBody Doctor doctor){
        return service.updateDoctor(id, doctor);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.deleteDoctor(id);
		System.out.println("aman");
    }
}

