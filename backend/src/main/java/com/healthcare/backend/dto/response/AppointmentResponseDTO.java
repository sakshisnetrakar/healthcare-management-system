package com.healthcare.backend.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.healthcare.backend.enums.AppointmentStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentResponseDTO {

    private Long id;

    private LocalDate appointmentDate;

    private LocalTime appointmentTime;

    private AppointmentStatus status;

    private Long doctorId;
    private String doctorName;

    private Long patientId;
    private String patientName;
}