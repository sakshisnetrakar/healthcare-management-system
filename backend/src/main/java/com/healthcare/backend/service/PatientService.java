package com.healthcare.backend.service;

import com.healthcare.backend.entity.Patient;

import java.util.List;

public interface PatientService {

    Patient addPatient(Patient patient);

    List<Patient> getAllPatients();

    Patient getPatientById(Long id);

    void deletePatient(Long id);
}