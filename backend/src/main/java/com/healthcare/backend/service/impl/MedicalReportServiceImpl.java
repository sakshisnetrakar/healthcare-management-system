package com.healthcare.backend.service.impl;

import com.healthcare.backend.entity.MedicalReport;
import com.healthcare.backend.repository.MedicalReportRepository;
import com.healthcare.backend.service.MedicalReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalReportServiceImpl implements MedicalReportService {

    @Autowired
    private MedicalReportRepository medicalReportRepository;

    @Override
    public MedicalReport saveReport(MedicalReport report) {
        return medicalReportRepository.save(report);
    }

    @Override
    public List<MedicalReport> getAllReports() {
        return medicalReportRepository.findAll();
    }

    @Override
    public MedicalReport getReportById(Long id) {
        return medicalReportRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteReport(Long id) {
        medicalReportRepository.deleteById(id);
    }
}