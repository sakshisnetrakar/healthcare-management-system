package com.healthcare.backend.dto.request;

import com.healthcare.backend.enums.Gender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRequestDTO {

    @NotBlank(message = "Patient name is required")
    private String patientName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age must not exceed 120")
    private int age;

    @NotBlank(message = "Blood group is required")
    private String bloodGroup;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must contain exactly 10 digits"
    )
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    private String address;

    private Long userId;
}