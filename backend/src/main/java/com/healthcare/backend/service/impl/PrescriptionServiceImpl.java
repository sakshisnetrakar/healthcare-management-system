package com.healthcare.backend.service.impl;

import com.healthcare.backend.entity.Appointment;
import com.healthcare.backend.entity.Doctor;
import com.healthcare.backend.entity.Patient;
import com.healthcare.backend.entity.Prescription;
import com.healthcare.backend.entity.User;

import com.healthcare.backend.repository.DoctorRepository;
import com.healthcare.backend.repository.PatientRepository;
import com.healthcare.backend.repository.PrescriptionRepository;
import com.healthcare.backend.repository.UserRepository;

import com.healthcare.backend.service.PrescriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;


    @Override
    public Prescription savePrescription(
            Prescription prescription) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        // Appointment is required
        if (prescription.getAppointment() == null ||
                prescription.getAppointment().getId() == null) {

            throw new RuntimeException(
                    "Appointment ID is required");
        }


        // ADMIN can create any prescription
        if (user.getRole().name().equals("ADMIN")) {

            return prescriptionRepository.save(prescription);
        }


        // DOCTOR can create prescription
        // only for their own appointment
        if (user.getRole().name().equals("DOCTOR")) {

            Doctor doctor = doctorRepository
                    .findByUserId(user.getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Doctor record not found"));

            Appointment appointment =
                    prescription.getAppointment();

            if (appointment.getDoctor() == null ||
                    !appointment.getDoctor()
                            .getId()
                            .equals(doctor.getId())) {

                throw new AccessDeniedException(
                        "You can create prescriptions only for your own appointments");
            }

            return prescriptionRepository.save(prescription);
        }


        throw new AccessDeniedException(
                "Only ADMIN or DOCTOR can create prescriptions");
    }


    @Override
    public List<Prescription> getAllPrescriptions() {

        return prescriptionRepository.findAll();
    }


    @Override
    public Prescription getPrescriptionById(Long id) {

        Prescription prescription =
                prescriptionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Prescription not found"));


        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        String role = user.getRole().name();


        // ADMIN can access anything
        if (role.equals("ADMIN")) {
            return prescription;
        }


        Appointment appointment =
                prescription.getAppointment();

        if (appointment == null) {

            throw new RuntimeException(
                    "Prescription is not linked to an appointment");
        }


        // PATIENT → only their own prescription
        if (role.equals("PATIENT")) {

            Patient patient = patientRepository
                    .findByUserId(user.getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Patient record not found"));

            if (appointment.getPatient() == null ||
                    !appointment.getPatient()
                            .getId()
                            .equals(patient.getId())) {

                throw new AccessDeniedException(
                        "You can access only your own prescriptions");
            }

            return prescription;
        }


        // DOCTOR → only prescriptions
        // from their own appointments
        if (role.equals("DOCTOR")) {

            Doctor doctor = doctorRepository
                    .findByUserId(user.getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Doctor record not found"));

            if (appointment.getDoctor() == null ||
                    !appointment.getDoctor()
                            .getId()
                            .equals(doctor.getId())) {

                throw new AccessDeniedException(
                        "You can access only prescriptions from your appointments");
            }

            return prescription;
        }


        throw new AccessDeniedException(
                "Access denied");
    }


    @Override
    public void deletePrescription(Long id) {

        prescriptionRepository.deleteById(id);
    }
}