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
                        new RuntimeException(
                                "Patient record not found"));

        // Make sure the appointment uses the logged-in patient
        appointment.setPatient(patient);

        // Make sure the doctor exists
        if (appointment.getDoctor() == null ||
                appointment.getDoctor().getId() == null) {

            throw new RuntimeException(
                    "Doctor ID is required");
        }

        Doctor doctor = doctorRepository
                .findById(appointment.getDoctor().getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Doctor not found"));

        // Set the actual Doctor entity
        appointment.setDoctor(doctor);

        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAllAppointments() {

        return appointmentRepository.findAll();
    }

    @Override
    public Appointment getAppointmentById(Long id) {

        return appointmentRepository.findById(id)
                .orElse(null);
    }

    @Override
    public void deleteAppointment(Long id) {

        appointmentRepository.deleteById(id);
    }
}