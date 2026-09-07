package com.healthcare.backend.service;

import com.healthcare.backend.dto.request.PrescriptionRequestDTO;
import com.healthcare.backend.dto.response.PrescriptionResponseDTO;

import java.util.List;

public interface PrescriptionService {

    PrescriptionResponseDTO addPrescription(
            PrescriptionRequestDTO dto);

    List<PrescriptionResponseDTO> getAllPrescriptions();

    PrescriptionResponseDTO getPrescriptionById(Long id);

    void deletePrescription(Long id);
}