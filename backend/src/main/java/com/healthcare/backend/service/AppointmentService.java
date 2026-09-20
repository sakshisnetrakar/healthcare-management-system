package com.healthcare.backend.service;

import com.healthcare.backend.dto.request.AppointmentRequestDTO;
import com.healthcare.backend.dto.response.AppointmentResponseDTO;

import java.util.List;

public interface AppointmentService {

    AppointmentResponseDTO bookAppointment(
            AppointmentRequestDTO dto);

    List<AppointmentResponseDTO> getAllAppointments();

    AppointmentResponseDTO getAppointmentById(Long id);

    List<AppointmentResponseDTO> getAppointmentsByDoctor(
            Long doctorId);

    List<AppointmentResponseDTO> getAppointmentsByPatient(
            Long patientId);

    AppointmentResponseDTO completeAppointment(Long id);

    AppointmentResponseDTO cancelAppointment(Long id);

    void deleteAppointment(Long id);
}