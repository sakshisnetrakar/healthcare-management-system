package com.healthcare.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionRequestDTO {

    @NotBlank(message = "Medicine is required")
    private String medicine;

    @NotBlank(message = "Dosage is required")
    private String dosage;

    @NotBlank(message = "Instructions are required")
    private String instructions;

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;
}