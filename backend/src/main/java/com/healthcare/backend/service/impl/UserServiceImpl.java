package com.healthcare.backend.service.impl;

import com.healthcare.backend.dto.request.UserRequestDTO;
import com.healthcare.backend.dto.response.UserResponseDTO;
import com.healthcare.backend.entity.User;
import com.healthcare.backend.exception.ResourceNotFoundException;
import com.healthcare.backend.mapper.UserMapper;
import com.healthcare.backend.repository.UserRepository;
import com.healthcare.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // Register a new user
    @Override
    public UserResponseDTO registerUser(UserRequestDTO dto) {

        User user = UserMapper.toEntity(dto);

        User savedUser = userRepository.save(user);

        return UserMapper.toResponseDTO(savedUser);
    }

    // Get user by ID
    @Override
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        return UserMapper.toResponseDTO(user);
    }

    // Get all users
    @Override
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .toList();
    }

    // Delete user
   @Override
    public void deleteUser(Long id) {

    User user = userRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "User not found with id: " + id
                    )
            );

    userRepository.delete(user);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

    User user = userRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "User not found with id: " + id
                    )
            );

    user.setFirstName(dto.getFirstName());
    user.setLastName(dto.getLastName());
    user.setEmail(dto.getEmail());
    user.setPhoneNumber(dto.getPhoneNumber());
    user.setRole(dto.getRole());

    User updatedUser = userRepository.save(user);

    return UserMapper.toResponseDTO(updatedUser);
    }
}