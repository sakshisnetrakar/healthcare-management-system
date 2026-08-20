package com.healthcare.backend.service.impl;

import com.healthcare.backend.entity.Appointment;
import com.healthcare.backend.entity.Doctor;
import com.healthcare.backend.entity.Patient;
import com.healthcare.backend.entity.User;
import com.healthcare.backend.repository.AppointmentRepository;
import com.healthcare.backend.repository.DoctorRepository;
import com.healthcare.backend.repository.PatientRepository;
import com.healthcare.backend.repository.UserRepository;
import com.healthcare.backend.service.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;


    @Override
    public Appointment bookAppointment(Appointment appointment) {

        // Get logged-in user's email from JWT
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        // Find logged-in User
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Find Patient linked to this User
        Patient patient = patientRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new RuntimeException("Patient record not found"));

        // Automatically set the logged-in patient
        appointment.setPatient(patient);

        // Check doctor ID
        if (appointment.getDoctor() == null ||
                appointment.getDoctor().getId() == null) {

            throw new RuntimeException("Doctor ID is required");
        }

        // Find Doctor
        Doctor doctor = doctorRepository
                .findById(appointment.getDoctor().getId())
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found"));

        // Set actual Doctor entity
        appointment.setDoctor(doctor);

        return appointmentRepository.save(appointment);
    }


    @Override
    public List<Appointment> getAllAppointments() {

        return appointmentRepository.findAll();
    }


    @Override
    public Appointment getAppointmentById(Long id) {

        // Find appointment
        Appointment appointment = appointmentRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found"));

        // Get logged-in user from JWT
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        // Find User
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        String role = user.getRole().name();


        // ADMIN can access any appointment
        if (role.equals("ADMIN")) {
            return appointment;
        }


        // PATIENT can access only their own appointment
        if (role.equals("PATIENT")) {

            Patient patient = patientRepository
                    .findByUserId(user.getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Patient record not found"));

            if (!appointment.getPatient()
                    .getId()
                    .equals(patient.getId())) {

                throw new AccessDeniedException(
                        "You can access only your own appointments");
            }

            return appointment;
        }


        // DOCTOR can access only their own appointments
        if (role.equals("DOCTOR")) {

            Doctor doctor = doctorRepository
                    .findByUserId(user.getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Doctor record not found"));

            if (!appointment.getDoctor()
                    .getId()
                    .equals(doctor.getId())) {

                throw new AccessDeniedException(
                        "You can access only your own appointments");
            }

            return appointment;
        }


        throw new AccessDeniedException("Access denied");
    }


    @Override
    public void deleteAppointment(Long id) {

        appointmentRepository.deleteById(id);
    }
}