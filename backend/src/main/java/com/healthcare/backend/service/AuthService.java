package com.healthcare.backend.service;

import com.healthcare.backend.dto.request.LoginRequestDTO;
import com.healthcare.backend.dto.response.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO dto);
}