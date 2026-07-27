package com.healthcare.backend.repository;

import com.healthcare.backend.entity.MedicalReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalReportRepository extends JpaRepository<MedicalReport, Long> {

}