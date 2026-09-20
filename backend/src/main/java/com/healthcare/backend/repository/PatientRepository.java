package com.healthcare.backend.repository;

import com.healthcare.backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository
        extends JpaRepository<Patient, Long> {

    Optional<Patient> findByUserId(Long userId);

    Optional<Patient> findByPhoneNumber(
            String phoneNumber);
}