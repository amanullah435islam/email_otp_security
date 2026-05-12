package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "doctor")

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Doctor {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "doctor_code")
	private int doctorCode;
	
	@Column(name = "doctor_name")
	private String doctorName;
	

	@Column(name = "specialize")
	private String specialize;

	@Column(name = "contact")
	private String contact;

	@Column(name = "availability")
	private String availability;

	@Column(name = "email")
	private String email;

	@Column(name = "roomNumber")
	private String roomNumber;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "image")
	private String image;
	
}