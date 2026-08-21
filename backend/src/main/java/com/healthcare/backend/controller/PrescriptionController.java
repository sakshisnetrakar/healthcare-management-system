package com.healthcare.backend.controller;

import com.healthcare.backend.entity.Prescription;
import com.healthcare.backend.service.PrescriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;


    // DOCTOR and ADMIN can create prescriptions
    @PreAuthorize("hasAnyRole(\"DOCTOR\", \"ADMIN\")")
    @PostMapping
    public Prescription addPrescription(
            @RequestBody Prescription prescription) {

        return prescriptionService.savePrescription(prescription);
    }


    // Only ADMIN can view all prescriptions
    @PreAuthorize("hasRole(\"ADMIN\")")
    @GetMapping
    public List<Prescription> getAllPrescriptions() {

        return prescriptionService.getAllPrescriptions();
    }


    // ADMIN, DOCTOR and PATIENT can request one prescription
    @PreAuthorize("hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @GetMapping("/{id}")
    public Prescription getPrescriptionById(
            @PathVariable Long id) {

        return prescriptionService.getPrescriptionById(id);
    }


    // Only ADMIN can delete
    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("/{id}")
    public String deletePrescription(
            @PathVariable Long id) {

        prescriptionService.deletePrescription(id);

        return "Prescription deleted successfully";
    }
}