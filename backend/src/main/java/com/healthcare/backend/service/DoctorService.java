package com.healthcare.backend.service;

import com.healthcare.backend.dto.request.DoctorRequestDTO;
import com.healthcare.backend.dto.response.DoctorResponseDTO;

import java.util.List;

public interface DoctorService {

    DoctorResponseDTO addDoctor(
            DoctorRequestDTO dto);

    List<DoctorResponseDTO> getAllDoctors();

    DoctorResponseDTO getDoctorById(Long id);

    List<DoctorResponseDTO> searchBySpecialization(
            String specialization);

    List<DoctorResponseDTO> getDoctorsByDepartment(
            Long departmentId);

    void deleteDoctor(Long id);
}