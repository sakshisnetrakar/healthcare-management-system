package com.healthcare.backend.service;

import com.healthcare.backend.entity.Department;

import java.util.List;

public interface DepartmentService {

    Department addDepartment(Department department);

    List<Department> getAllDepartments();

    Department getDepartmentById(Long id);

    void deleteDepartment(Long id);
}