package com.healthcare.backend.dto.response;

import com.healthcare.backend.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Role role;

    private Boolean active;

    private LocalDateTime createdAt;
}