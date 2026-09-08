package com.healthcare.backend.service.impl;

import com.healthcare.backend.dto.request.DepartmentRequestDTO;
import com.healthcare.backend.dto.response.DepartmentResponseDTO;
import com.healthcare.backend.entity.Department;
import com.healthcare.backend.mapper.DepartmentMapper;
import com.healthcare.backend.repository.DepartmentRepository;
import com.healthcare.backend.service.DepartmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl
        implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;


    @Override
    public DepartmentResponseDTO addDepartment(
            DepartmentRequestDTO dto) {

        Department department =
                DepartmentMapper.toEntity(dto);

        Department savedDepartment =
                departmentRepository.save(department);

        return DepartmentMapper.toResponseDTO(
                savedDepartment);
    }


    @Override
    public List<DepartmentResponseDTO>
    getAllDepartments() {

        return departmentRepository.findAll()
                .stream()
                .map(DepartmentMapper::toResponseDTO)
                .toList();
    }


    @Override
    public DepartmentResponseDTO getDepartmentById(
            Long id) {

        Department department =
                departmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Department not found"));

        return DepartmentMapper.toResponseDTO(
                department);
    }


    @Override
    public void deleteDepartment(Long id) {

        if (!departmentRepository.existsById(id)) {

            throw new RuntimeException(
                    "Department not found");
        }

        departmentRepository.deleteById(id);
    }
}