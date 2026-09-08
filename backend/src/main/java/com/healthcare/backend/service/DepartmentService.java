package com.healthcare.backend.service;

import com.healthcare.backend.dto.request.DepartmentRequestDTO;
import com.healthcare.backend.dto.response.DepartmentResponseDTO;

import java.util.List;

public interface DepartmentService {

    DepartmentResponseDTO addDepartment(
            DepartmentRequestDTO dto);

    List<DepartmentResponseDTO> getAllDepartments();

    DepartmentResponseDTO getDepartmentById(Long id);

    void deleteDepartment(Long id);
}