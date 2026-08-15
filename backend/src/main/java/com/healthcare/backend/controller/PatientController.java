package com.healthcare.backend.controller;

import com.healthcare.backend.entity.Patient;
import com.healthcare.backend.service.PatientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    // Only ADMIN can add a patient
    @PreAuthorize("hasRole(\"ADMIN\")")
    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {

        return patientService.addPatient(patient);
    }

    // ADMIN and DOCTOR can view all patients
    @PreAuthorize("hasAnyRole(\"ADMIN\", \"DOCTOR\")")
    @GetMapping
    public List<Patient> getAllPatients() {

        return patientService.getAllPatients();
    }

    // ADMIN, DOCTOR and PATIENT can view a patient
    @PreAuthorize("hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id) {

        return patientService.getPatientById(id);
    }

    // Only ADMIN can delete a patient
    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable Long id) {

        patientService.deletePatient(id);

        return "Patient deleted successfully";
    }
}