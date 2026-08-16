package com.healthcare.backend.service.impl;

import com.healthcare.backend.entity.Doctor;
import com.healthcare.backend.entity.User;
import com.healthcare.backend.enums.Role;
import com.healthcare.backend.repository.DoctorRepository;
import com.healthcare.backend.repository.UserRepository;
import com.healthcare.backend.service.DoctorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Doctor addDoctor(Doctor doctor, Long userId) {

        // Find User
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Make sure the User is actually a DOCTOR
        if (user.getRole() != Role.DOCTOR) {
            throw new RuntimeException(
                    "Selected user does not have DOCTOR role");
        }

        // Connect Doctor with User
        doctor.setUser(user);

        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {

        return doctorRepository.findAll();
    }

    @Override
    public Doctor getDoctorById(Long id) {

        return doctorRepository.findById(id)
                .orElse(null);
    }

    @Override
    public void deleteDoctor(Long id) {

        doctorRepository.deleteById(id);
    }
}