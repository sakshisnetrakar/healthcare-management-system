package com.healthcare.backend.service;

import com.healthcare.backend.dto.request.LoginRequestDTO;
import com.healthcare.backend.dto.response.LoginResponseDTO;
import com.healthcare.backend.entity.User;
import com.healthcare.backend.enums.Role;
import com.healthcare.backend.repository.UserRepository;
import com.healthcare.backend.security.jwt.JwtUtil;
import com.healthcare.backend.service.impl.AuthServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@gmail.com");
        request.setPassword("123456");

        User user = new User();

        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.PATIENT);
        user.setActive(true);

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "123456",
                "encodedPassword"))
                .thenReturn(true);

        when(jwtUtil.generateToken(
                "test@gmail.com",
                "PATIENT"))
                .thenReturn("dummy-jwt-token");

        LoginResponseDTO response =
                authService.login(request);

        assertNotNull(response);
        assertEquals("dummy-jwt-token", response.getToken());
        assertEquals("Login successful", response.getMessage());

        verify(userRepository)
                .findByEmail("test@gmail.com");

        verify(passwordEncoder)
                .matches("123456", "encodedPassword");

        verify(jwtUtil)
                .generateToken(
                        "test@gmail.com",
                        "PATIENT");
    }
}