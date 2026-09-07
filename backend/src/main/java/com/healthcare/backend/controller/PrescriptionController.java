package com.healthcare.backend.controller;

import com.healthcare.backend.dto.request.PrescriptionRequestDTO;
import com.healthcare.backend.dto.response.PrescriptionResponseDTO;
import com.healthcare.backend.service.PrescriptionService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;


    // ADMIN and DOCTOR can create
    @PreAuthorize("hasAnyRole(\"ADMIN\", \"DOCTOR\")")
    @PostMapping
    public PrescriptionResponseDTO addPrescription(
            @Valid @RequestBody PrescriptionRequestDTO dto) {

        return prescriptionService.addPrescription(dto);
    }


    // Only ADMIN can view all
    @PreAuthorize("hasRole(\"ADMIN\")")
    @GetMapping
    public List<PrescriptionResponseDTO>
    getAllPrescriptions() {

        return prescriptionService
                .getAllPrescriptions();
    }


    // ADMIN, DOCTOR and PATIENT
    @PreAuthorize(
            "hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @GetMapping("/{id}")
    public PrescriptionResponseDTO
    getPrescriptionById(
            @PathVariable Long id) {

        return prescriptionService
                .getPrescriptionById(id);
    }


    // Only ADMIN can delete
    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("/{id}")
    public String deletePrescription(
            @PathVariable Long id) {

        prescriptionService
                .deletePrescription(id);

        return "Prescription deleted successfully";
    }
}