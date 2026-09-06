package com.healthcare.backend.mapper;

import com.healthcare.backend.dto.request.DoctorRequestDTO;
import com.healthcare.backend.dto.response.DoctorResponseDTO;
import com.healthcare.backend.entity.Doctor;

public class DoctorMapper {

    public static Doctor toEntity(DoctorRequestDTO dto) {

        Doctor doctor = new Doctor();

        doctor.setDoctorName(dto.getDoctorName());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setQualification(dto.getQualification());
        doctor.setExperience(dto.getExperience());

        return doctor;
    }


    public static DoctorResponseDTO toResponseDTO(
            Doctor doctor) {

        DoctorResponseDTO dto =
                new DoctorResponseDTO();

        dto.setId(doctor.getId());
        dto.setDoctorName(doctor.getDoctorName());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setQualification(doctor.getQualification());
        dto.setExperience(doctor.getExperience());

        if (doctor.getDepartment() != null) {

            dto.setDepartmentId(
                    doctor.getDepartment().getId());

            dto.setDepartmentName(
                    doctor.getDepartment()
                            .getDepartmentName());
        }

        if (doctor.getUser() != null) {

            dto.setUserId(
                    doctor.getUser().getId());
        }

        return dto;
    }
}