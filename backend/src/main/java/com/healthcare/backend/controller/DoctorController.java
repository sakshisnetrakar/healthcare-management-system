package com.healthcare.backend.controller;

import com.healthcare.backend.entity.Doctor;
import com.healthcare.backend.service.DoctorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // Only ADMIN can add a doctor
    @PreAuthorize("hasRole(\"ADMIN\")")
    @PostMapping
    public Doctor addDoctor(
            @RequestBody Doctor doctor,
            @RequestParam Long userId) {

        return doctorService.addDoctor(doctor, userId);
    }

    // ADMIN, DOCTOR and PATIENT can view all doctors
    @PreAuthorize("hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @GetMapping
    public List<Doctor> getAllDoctors() {

        return doctorService.getAllDoctors();
    }

    // ADMIN, DOCTOR and PATIENT can view a doctor
    @PreAuthorize("hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable Long id) {

        return doctorService.getDoctorById(id);
    }

    // Only ADMIN can delete a doctor
    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("/{id}")
    public String deleteDoctor(@PathVariable Long id) {

        doctorService.deleteDoctor(id);

        return "Doctor deleted successfully";
    }
}