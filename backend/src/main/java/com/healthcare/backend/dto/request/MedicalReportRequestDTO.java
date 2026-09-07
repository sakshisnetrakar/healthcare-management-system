package com.healthcare.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalReportRequestDTO {

    @NotBlank(message = "Report name is required")
    private String reportName;

    @NotBlank(message = "Report type is required")
    private String reportType;

    @NotBlank(message = "Findings are required")
    @Size(max = 1000, message = "Findings cannot exceed 1000 characters")
    private String findings;

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;
}