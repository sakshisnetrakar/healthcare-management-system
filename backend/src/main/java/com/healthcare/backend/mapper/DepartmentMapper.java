package com.healthcare.backend.mapper;

import com.healthcare.backend.dto.request.DepartmentRequestDTO;
import com.healthcare.backend.dto.response.DepartmentResponseDTO;
import com.healthcare.backend.entity.Department;

public class DepartmentMapper {

    public static Department toEntity(
            DepartmentRequestDTO dto) {

        Department department = new Department();

        department.setDepartmentName(
                dto.getDepartmentName());

        department.setDescription(
                dto.getDescription());

        return department;
    }


    public static DepartmentResponseDTO toResponseDTO(
            Department department) {

        DepartmentResponseDTO dto =
                new DepartmentResponseDTO();

        dto.setId(department.getId());

        dto.setDepartmentName(
                department.getDepartmentName());

        dto.setDescription(
                department.getDescription());

        return dto;
    }
}