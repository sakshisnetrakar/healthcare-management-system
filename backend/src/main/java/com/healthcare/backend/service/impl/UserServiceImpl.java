package com.healthcare.backend.service.impl;

import com.healthcare.backend.dto.request.UserRequestDTO;
import com.healthcare.backend.dto.response.UserResponseDTO;
import com.healthcare.backend.entity.User;
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

    @Override
    public UserResponseDTO registerUser(UserRequestDTO dto) {

        User user = UserMapper.toEntity(dto);

        User savedUser = userRepository.save(user);

        return UserMapper.toResponseDTO(savedUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .toList();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id).orElse(null);

        if(user == null)
            return null;

        return UserMapper.toResponseDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}