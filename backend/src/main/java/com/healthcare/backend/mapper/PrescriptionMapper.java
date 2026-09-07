package com.healthcare.backend.mapper;

import com.healthcare.backend.dto.request.PrescriptionRequestDTO;
import com.healthcare.backend.dto.response.PrescriptionResponseDTO;
import com.healthcare.backend.entity.Prescription;

public class PrescriptionMapper {

    public static Prescription toEntity(
            PrescriptionRequestDTO dto) {

        Prescription prescription = new Prescription();

        prescription.setMedicine(dto.getMedicine());
        prescription.setDosage(dto.getDosage());
        prescription.setInstructions(dto.getInstructions());

        return prescription;
    }


    public static PrescriptionResponseDTO toResponseDTO(
            Prescription prescription) {

        PrescriptionResponseDTO dto =
                new PrescriptionResponseDTO();

        dto.setId(prescription.getId());
        dto.setMedicine(prescription.getMedicine());
        dto.setDosage(prescription.getDosage());
        dto.setInstructions(prescription.getInstructions());

        if (prescription.getAppointment() != null) {

            dto.setAppointmentId(
                    prescription.getAppointment().getId());

            if (prescription.getAppointment().getPatient() != null) {

                dto.setPatientId(
                        prescription.getAppointment()
                                .getPatient()
                                .getId());

                dto.setPatientName(
                        prescription.getAppointment()
                                .getPatient()
                                .getPatientName());
            }

            if (prescription.getAppointment().getDoctor() != null) {

                dto.setDoctorId(
                        prescription.getAppointment()
                                .getDoctor()
                                .getId());

                dto.setDoctorName(
                        prescription.getAppointment()
                                .getDoctor()
                                .getDoctorName());
            }
        }

        return dto;
    }
}