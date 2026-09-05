package com.healthcare.backend.mapper;

import com.healthcare.backend.dto.response.AppointmentResponseDTO;
import com.healthcare.backend.entity.Appointment;

public class AppointmentMapper {

    public static AppointmentResponseDTO toResponseDTO(
            Appointment appointment) {

        AppointmentResponseDTO dto =
                new AppointmentResponseDTO();

        dto.setId(appointment.getId());
        dto.setAppointmentDate(
                appointment.getAppointmentDate());
        dto.setAppointmentTime(
                appointment.getAppointmentTime());
        dto.setStatus(appointment.getStatus());

        if (appointment.getDoctor() != null) {

            dto.setDoctorId(
                    appointment.getDoctor().getId());

            dto.setDoctorName(
                    appointment.getDoctor().getDoctorName());
        }

        if (appointment.getPatient() != null) {

            dto.setPatientId(
                    appointment.getPatient().getId());

            dto.setPatientName(
                    appointment.getPatient().getPatientName());
        }

        return dto;
    }
}