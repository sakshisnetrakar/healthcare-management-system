package com.healthcare.backend.controller;

import com.healthcare.backend.entity.MedicalReport;
import com.healthcare.backend.service.MedicalReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class MedicalReportController {

    @Autowired
    private MedicalReportService medicalReportService;


    // DOCTOR and ADMIN can add reports
    @PreAuthorize("hasAnyRole(\"DOCTOR\", \"ADMIN\")")
    @PostMapping
    public MedicalReport addReport(
            @RequestBody MedicalReport report) {

        return medicalReportService.saveReport(report);
    }


    // Only ADMIN can view all reports
    @PreAuthorize("hasRole(\"ADMIN\")")
    @GetMapping
    public List<MedicalReport> getAllReports() {

        return medicalReportService.getAllReports();
    }


    // ADMIN, DOCTOR and PATIENT can request a specific report
    @PreAuthorize("hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @GetMapping("/{id}")
    public MedicalReport getReportById(
            @PathVariable Long id) {

        return medicalReportService.getReportById(id);
    }


    // Only ADMIN can delete
    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("/{id}")
    public String deleteReport(
            @PathVariable Long id) {

        medicalReportService.deleteReport(id);

        return "Medical report deleted successfully";
    }
}