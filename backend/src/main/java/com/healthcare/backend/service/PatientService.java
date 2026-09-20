package com.healthcare.backend.service;

import com.healthcare.backend.dto.request.PatientRequestDTO;
import com.healthcare.backend.dto.response.PatientResponseDTO;

import java.util.List;

public interface PatientService {

    PatientResponseDTO addPatient(
            PatientRequestDTO dto);

    List<PatientResponseDTO> getAllPatients();

    PatientResponseDTO getPatientById(Long id);

    PatientResponseDTO getPatientByPhoneNumber(
            String phoneNumber);

    void deletePatient(Long id);
}