package com.healthcare.backend.repository;

import com.healthcare.backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository
        extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByUserId(Long userId);

    List<Doctor> findBySpecializationIgnoreCase(
            String specialization);

    List<Doctor> findByDepartmentId(
            Long departmentId);
}