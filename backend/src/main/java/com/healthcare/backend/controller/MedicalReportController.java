package com.healthcare.backend.controller;

import com.healthcare.backend.dto.request.MedicalReportRequestDTO;
import com.healthcare.backend.dto.response.MedicalReportResponseDTO;
import com.healthcare.backend.service.MedicalReportService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-reports")
public class MedicalReportController {

    @Autowired
    private MedicalReportService medicalReportService;


    // ADMIN and DOCTOR can create
    @PreAuthorize("hasAnyRole(\"ADMIN\", \"DOCTOR\")")
    @PostMapping
    public MedicalReportResponseDTO addMedicalReport(
            @Valid @RequestBody MedicalReportRequestDTO dto) {

        return medicalReportService.addMedicalReport(dto);
    }


    // Only ADMIN can view all
    @PreAuthorize("hasRole(\"ADMIN\")")
    @GetMapping
    public List<MedicalReportResponseDTO>
    getAllMedicalReports() {

        return medicalReportService
                .getAllMedicalReports();
    }


    // ADMIN, DOCTOR and PATIENT
    @PreAuthorize(
            "hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @GetMapping("/{id}")
    public MedicalReportResponseDTO
    getMedicalReportById(
            @PathVariable Long id) {

        return medicalReportService
                .getMedicalReportById(id);
    }


    // Only ADMIN can delete
    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("/{id}")
    public String deleteMedicalReport(
            @PathVariable Long id) {

        medicalReportService
                .deleteMedicalReport(id);

        return "Medical report deleted successfully";
    }
}