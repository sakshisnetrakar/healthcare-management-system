package com.healthcare.backend.repository;

import com.healthcare.backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query(
        value = "SELECT * FROM patients WHERE user_id = :userId",
        nativeQuery = true
    )
    Optional<Patient> findByUserId(@Param("userId") Long userId);
}