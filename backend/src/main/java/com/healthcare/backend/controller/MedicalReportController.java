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


    // ADMIN and DOCTOR can add medical reports
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @PostMapping
    public MedicalReport addReport(
            @RequestBody MedicalReport report) {

        return medicalReportService.saveReport(report);
    }


    // Only ADMIN can view all reports
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<MedicalReport> getAllReports() {

        return medicalReportService.getAllReports();
    }


    // ADMIN, DOCTOR and PATIENT can request a report
    // Ownership checking will be done in service
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{id}")
    public MedicalReport getReportById(
            @PathVariable Long id) {

        return medicalReportService.getReportById(id);
    }


    // Only ADMIN can delete reports
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteReport(
            @PathVariable Long id) {

        medicalReportService.deleteReport(id);

        return "Medical report deleted successfully";
    }
}