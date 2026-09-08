package com.healthcare.backend.controller;

import com.healthcare.backend.dto.request.DepartmentRequestDTO;
import com.healthcare.backend.dto.response.DepartmentResponseDTO;
import com.healthcare.backend.service.DepartmentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;


    @PostMapping
    public DepartmentResponseDTO addDepartment(
            @Valid @RequestBody DepartmentRequestDTO dto) {

        return departmentService.addDepartment(dto);
    }


    @GetMapping
    public List<DepartmentResponseDTO>
    getAllDepartments() {

        return departmentService
                .getAllDepartments();
    }


    @GetMapping("/{id}")
    public DepartmentResponseDTO getDepartmentById(
            @PathVariable Long id) {

        return departmentService
                .getDepartmentById(id);
    }


    @DeleteMapping("/{id}")
    public String deleteDepartment(
            @PathVariable Long id) {

        departmentService.deleteDepartment(id);

        return "Department deleted successfully";
    }
}