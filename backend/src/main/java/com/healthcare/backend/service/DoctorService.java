package com.healthcare.backend.service;

import com.healthcare.backend.entity.Doctor;

import java.util.List;

public interface DoctorService {

    Doctor addDoctor(Doctor doctor);

    List<Doctor> getAllDoctors();

    Doctor getDoctorById(Long id);

    void deleteDoctor(Long id);
}