package com.healthcare.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionResponseDTO {

    private Long id;

    private String medicine;

    private String dosage;

    private String instructions;

    private Long appointmentId;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;
}