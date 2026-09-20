package com.healthcare.backend.controller;

import com.healthcare.backend.dto.request.DoctorRequestDTO;
import com.healthcare.backend.dto.response.DoctorResponseDTO;
import com.healthcare.backend.service.DoctorService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;


    // Only ADMIN can add doctor
    @PreAuthorize("hasRole(\"ADMIN\")")
    @PostMapping
    public DoctorResponseDTO addDoctor(
            @Valid @RequestBody DoctorRequestDTO dto) {

        return doctorService.addDoctor(dto);
    }


    // ADMIN, DOCTOR and PATIENT can view doctors
    @PreAuthorize(
            "hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @GetMapping
    public List<DoctorResponseDTO> getAllDoctors() {

        return doctorService.getAllDoctors();
    }

    @PreAuthorize(
        "hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @GetMapping("/search")
    public List<DoctorResponseDTO> searchBySpecialization(
        @RequestParam String specialization) {

        return doctorService
            .searchBySpecialization(specialization);
    }


    @PreAuthorize(
            "hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @GetMapping("/department/{departmentId}")
    public List<DoctorResponseDTO> getDoctorsByDepartment(
        @PathVariable Long departmentId) {

        return doctorService
            .getDoctorsByDepartment(departmentId);
    }

    // ADMIN, DOCTOR and PATIENT can view doctor
    @PreAuthorize(
            "hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @GetMapping("/{id}")
    public DoctorResponseDTO getDoctorById(
            @PathVariable Long id) {

        return doctorService.getDoctorById(id);
    }


    // Only ADMIN can delete
    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("/{id}")
    public String deleteDoctor(
            @PathVariable Long id) {

        doctorService.deleteDoctor(id);

        return "Doctor deleted successfully";
    }
}