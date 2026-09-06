package com.healthcare.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorResponseDTO {

    private Long id;

    private String doctorName;

    private String specialization;

    private String qualification;

    private int experience;

    private Long departmentId;
    private String departmentName;

    private Long userId;
}