package com.healthcare.backend.controller;

import com.healthcare.backend.dto.request.UserRequestDTO;
import com.healthcare.backend.dto.response.UserResponseDTO;
import com.healthcare.backend.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserResponseDTO registerUser(@Valid @RequestBody UserRequestDTO dto) {

        return userService.registerUser(dto);
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {

        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {

        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok("User deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
        @PathVariable Long id,
        @Valid @RequestBody UserRequestDTO dto) {

            return ResponseEntity.ok(userService.updateUser(id, dto));
    }
}