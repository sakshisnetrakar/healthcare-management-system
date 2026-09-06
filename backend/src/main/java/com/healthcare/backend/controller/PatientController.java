package com.healthcare.backend.controller;

import com.healthcare.backend.dto.request.PatientRequestDTO;
import com.healthcare.backend.dto.response.PatientResponseDTO;
import com.healthcare.backend.service.PatientService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;


    // Only ADMIN can add patient
    @PreAuthorize("hasRole(\"ADMIN\")")
    @PostMapping
    public PatientResponseDTO addPatient(
            @Valid @RequestBody PatientRequestDTO dto) {

        return patientService.addPatient(dto);
    }


    // ADMIN and DOCTOR can view all patients
    @PreAuthorize("hasAnyRole(\"ADMIN\", \"DOCTOR\")")
    @GetMapping
    public List<PatientResponseDTO> getAllPatients() {

        return patientService.getAllPatients();
    }


    // ADMIN, DOCTOR and PATIENT can view patient
    @PreAuthorize(
            "hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @GetMapping("/{id}")
    public PatientResponseDTO getPatientById(
            @PathVariable Long id) {

        return patientService.getPatientById(id);
    }


    // Only ADMIN can delete
    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("/{id}")
    public String deletePatient(
            @PathVariable Long id) {

        patientService.deletePatient(id);

        return "Patient deleted successfully";
    }
}