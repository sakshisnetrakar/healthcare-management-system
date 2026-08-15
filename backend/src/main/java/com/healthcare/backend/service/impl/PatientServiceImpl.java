package com.healthcare.backend.service.impl;

import com.healthcare.backend.entity.Patient;
import com.healthcare.backend.entity.User;
import com.healthcare.backend.repository.PatientRepository;
import com.healthcare.backend.repository.UserRepository;
import com.healthcare.backend.service.PatientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        String role = user.getRole().name();

        // ADMIN and DOCTOR can access any patient
        if (role.equals("ADMIN") || role.equals("DOCTOR")) {

            return patientRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Patient not found"));
        }

        // PATIENT can access only their own record
        if (role.equals("PATIENT")) {

            Patient patient = patientRepository.findByUserId(user.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Patient record not found"));

            if (!patient.getId().equals(id)) {
                throw new AccessDeniedException(
                        "You can access only your own patient record"
                );
            }

            return patient;
        }

        throw new AccessDeniedException("Access denied");
    }

    @Override
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}