package com.healthcare.backend.service;

import com.healthcare.backend.entity.Prescription;

import java.util.List;

public interface PrescriptionService {

    Prescription savePrescription(Prescription prescription);

    List<Prescription> getAllPrescriptions();

    Prescription getPrescriptionById(Long id);

    void deletePrescription(Long id);
}