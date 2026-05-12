package com.example.demo.testcontroller;


import com.example.demo.controller.DoctorController;
import com.example.demo.model.Doctor;
import com.example.demo.service.DoctorServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//note -1:--->!
//@WebMvcTest(
//        controllers = PatientController.class,
//        excludeAutoConfiguration = {
//                      SecurityConfig.class
//        } // Disables security completely for this test

// altenative:::::::::::::

//@WebMvcTest(DoctorController.class)
//@AutoConfigureMockMvc(addFilters = false)

//note -2:--->!
//@AutoConfigureMockMvc(addFilters = false)

//alternative:::::::::::::::::

//@WithMockUser
//or::::::::::::::
// @WithMockUser(username = "admin", roles = {"ADMIN"})

@WebMvcTest(DoctorController.class)
class TestControllerDoctor_alternative {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //  @MockBean support bellow the 3.4.0 version. I am testing   @MockBean using version 3.3.5
    //project location -> (C:\Users\USER\Desktop\Imran Sir_4-08-2026\demo(1)\demo)
    @MockBean
    private DoctorServiceImp service;

    private Doctor sampleDoctor;

    @BeforeEach
    void setUp() {
        sampleDoctor = new Doctor();
        sampleDoctor.setId(1L);
        sampleDoctor.setDoctorCode(152);
        sampleDoctor.setDoctorName("Aman Test");
    }

    @Test
    void testSaveDoctor() throws Exception {
        when(service.createDoctor(any(Doctor.class))).thenReturn(sampleDoctor);

        mockMvc.perform(post("/api/doctor/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDoctor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorName").value("Aman Test"));
    }

    @Test
    void testGetAll() throws Exception {

        when(service.getAllDoctor()).thenReturn(List.of());

        mockMvc.perform(get("/api/doctor/getAll"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {

        Doctor d = new Doctor();
        d.setId(1L);

        when(service.getDoctorById(1L)).thenReturn(d);

        mockMvc.perform(get("/api/doctor/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdate() throws Exception {

        Doctor d = new Doctor();
        d.setDoctorName("Updated");

        when(service.updateDoctor(eq(1L), any(Doctor.class))).thenReturn(d);

        mockMvc.perform(put("/api/doctor/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(d)))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {

        doNothing().when(service).deleteDoctor(1L);

        mockMvc.perform(delete("/api/doctor/1"))
                .andExpect(status().isOk());
    }
}