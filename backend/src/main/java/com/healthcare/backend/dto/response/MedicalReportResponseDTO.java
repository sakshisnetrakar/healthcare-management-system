package com.healthcare.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalReportResponseDTO {

    private Long id;

    private String reportName;

    private String reportType;

    private String findings;

    private Long appointmentId;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;
}