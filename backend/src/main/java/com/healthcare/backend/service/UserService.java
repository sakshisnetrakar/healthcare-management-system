package com.healthcare.backend.service;

import com.healthcare.backend.dto.request.UserRequestDTO;
import com.healthcare.backend.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO registerUser(UserRequestDTO dto);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    void deleteUser(Long id);

    UserResponseDTO updateUser(Long id, UserRequestDTO dto);
}