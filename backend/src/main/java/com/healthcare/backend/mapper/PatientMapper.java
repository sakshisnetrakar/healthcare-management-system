package com.healthcare.backend.mapper;

import com.healthcare.backend.dto.request.PatientRequestDTO;
import com.healthcare.backend.dto.response.PatientResponseDTO;
import com.healthcare.backend.entity.Patient;

public class PatientMapper {

    public static Patient toEntity(PatientRequestDTO dto) {

        Patient patient = new Patient();

        patient.setPatientName(dto.getPatientName());
        patient.setGender(dto.getGender());
        patient.setAge(dto.getAge());
        patient.setBloodGroup(dto.getBloodGroup());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setAddress(dto.getAddress());

        return patient;
    }


    public static PatientResponseDTO toResponseDTO(
            Patient patient) {

        PatientResponseDTO dto =
                new PatientResponseDTO();

        dto.setId(patient.getId());
        dto.setPatientName(patient.getPatientName());
        dto.setGender(patient.getGender());
        dto.setAge(patient.getAge());
        dto.setBloodGroup(patient.getBloodGroup());
        dto.setPhoneNumber(patient.getPhoneNumber());
        dto.setAddress(patient.getAddress());

        if (patient.getUser() != null) {
            dto.setUserId(patient.getUser().getId());
        }

        return dto;
    }
}