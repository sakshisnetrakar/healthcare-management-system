package com.healthcare.backend.service;

import com.healthcare.backend.dto.request.AppointmentRequestDTO;
import com.healthcare.backend.dto.response.AppointmentResponseDTO;

import java.util.List;

public interface AppointmentService {

    AppointmentResponseDTO bookAppointment(
            AppointmentRequestDTO dto);

    List<AppointmentResponseDTO> getAllAppointments();

    AppointmentResponseDTO getAppointmentById(Long id);

    void deleteAppointment(Long id);
}