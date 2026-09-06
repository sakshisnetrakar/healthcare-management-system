package com.healthcare.backend.service.impl;

import com.healthcare.backend.dto.request.PatientRequestDTO;
import com.healthcare.backend.dto.response.PatientResponseDTO;
import com.healthcare.backend.entity.Patient;
import com.healthcare.backend.entity.User;
import com.healthcare.backend.mapper.PatientMapper;
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
    public PatientResponseDTO addPatient(
            PatientRequestDTO dto) {

        Patient patient =
                PatientMapper.toEntity(dto);

        // Find User linked to patient
        if (dto.getUserId() == null) {
            throw new RuntimeException(
                    "User ID is required");
        }

        User user = userRepository
                .findById(dto.getUserId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        patient.setUser(user);

        Patient savedPatient =
                patientRepository.save(patient);

        return PatientMapper.toResponseDTO(
                savedPatient);
    }


    @Override
    public List<PatientResponseDTO> getAllPatients() {

        return patientRepository.findAll()
                .stream()
                .map(PatientMapper::toResponseDTO)
                .toList();
    }


    @Override
    public PatientResponseDTO getPatientById(Long id) {

        Patient patient =
                patientRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient not found"));

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        String role = user.getRole().name();


        // ADMIN can access any patient
        if (role.equals("ADMIN")) {

            return PatientMapper.toResponseDTO(
                    patient);
        }


        // DOCTOR can access any patient
        if (role.equals("DOCTOR")) {

            return PatientMapper.toResponseDTO(
                    patient);
        }


        // PATIENT can access only their own record
        if (role.equals("PATIENT")) {

            if (patient.getUser() == null ||
                    !patient.getUser()
                            .getId()
                            .equals(user.getId())) {

                throw new AccessDeniedException(
                        "You can access only your own patient record");
            }

            return PatientMapper.toResponseDTO(
                    patient);
        }


        throw new AccessDeniedException(
                "Access denied");
    }


    @Override
    public void deletePatient(Long id) {

        if (!patientRepository.existsById(id)) {

            throw new RuntimeException(
                    "Patient not found");
        }

        patientRepository.deleteById(id);
    }
}