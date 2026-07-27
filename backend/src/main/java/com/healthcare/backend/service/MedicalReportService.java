package com.healthcare.backend.service;

import com.healthcare.backend.entity.MedicalReport;

import java.util.List;

public interface MedicalReportService {

    MedicalReport saveReport(MedicalReport report);

    List<MedicalReport> getAllReports();

    MedicalReport getReportById(Long id);

    void deleteReport(Long id);
}