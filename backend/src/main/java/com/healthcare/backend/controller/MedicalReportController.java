package com.healthcare.backend.controller;

import com.healthcare.backend.entity.MedicalReport;
import com.healthcare.backend.service.MedicalReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class MedicalReportController {

    @Autowired
    private MedicalReportService medicalReportService;

    @PostMapping
    public MedicalReport addReport(@RequestBody MedicalReport report) {
        return medicalReportService.saveReport(report);
    }

    @GetMapping
    public List<MedicalReport> getAllReports() {
        return medicalReportService.getAllReports();
    }

    @GetMapping("/{id}")
    public MedicalReport getReportById(@PathVariable Long id) {
        return medicalReportService.getReportById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteReport(@PathVariable Long id) {
        medicalReportService.deleteReport(id);
        return "Medical report deleted successfully";
    }
}