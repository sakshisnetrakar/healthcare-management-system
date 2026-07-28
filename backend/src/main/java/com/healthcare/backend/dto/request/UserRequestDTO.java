package com.healthcare.backend.dto.request;

import com.healthcare.backend.enums.Role;
import lombok.Data;

@Data
public class UserRequestDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phoneNumber;

    private Role role;
}