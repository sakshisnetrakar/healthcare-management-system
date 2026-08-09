package com.healthcare.backend.service.impl;

import com.healthcare.backend.dto.request.LoginRequestDTO;
import com.healthcare.backend.dto.response.LoginResponseDTO;
import com.healthcare.backend.entity.User;
import com.healthcare.backend.exception.ResourceNotFoundException;
import com.healthcare.backend.repository.UserRepository;
import com.healthcare.backend.security.jwt.JwtUtil;
import com.healthcare.backend.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + dto.getEmail()
                        )
                );

        if (!passwordEncoder.matches(
                dto.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponseDTO(
                token,
                "Login successful"
        );
    }
}