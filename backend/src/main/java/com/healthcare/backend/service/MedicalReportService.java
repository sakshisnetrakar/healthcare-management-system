package com.healthcare.backend.service;

import com.healthcare.backend.dto.request.MedicalReportRequestDTO;
import com.healthcare.backend.dto.response.MedicalReportResponseDTO;

import java.util.List;

public interface MedicalReportService {

    MedicalReportResponseDTO addMedicalReport(
            MedicalReportRequestDTO dto);

    List<MedicalReportResponseDTO> getAllMedicalReports();

    MedicalReportResponseDTO getMedicalReportById(Long id);

    void deleteMedicalReport(Long id);
}