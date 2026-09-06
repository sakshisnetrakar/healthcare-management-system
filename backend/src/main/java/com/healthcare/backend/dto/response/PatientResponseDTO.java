package com.healthcare.backend.dto.response;

import com.healthcare.backend.enums.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientResponseDTO {

    private Long id;

    private String patientName;

    private Gender gender;

    private int age;

    private String bloodGroup;

    private String phoneNumber;

    private String address;

    private Long userId;
}