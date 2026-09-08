package com.healthcare.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentRequestDTO {

    @NotBlank(message = "Department name is required")
    private String departmentName;

    @NotBlank(message = "Description is required")
    private String description;
}