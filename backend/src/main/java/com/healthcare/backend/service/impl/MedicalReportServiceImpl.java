package com.healthcare.backend.service.impl;

import com.healthcare.backend.entity.MedicalReport;
import com.healthcare.backend.entity.Appointment;
import com.healthcare.backend.entity.Doctor;
import com.healthcare.backend.entity.Patient;
import com.healthcare.backend.entity.User;

import com.healthcare.backend.repository.MedicalReportRepository;
import com.healthcare.backend.repository.DoctorRepository;
import com.healthcare.backend.repository.PatientRepository;
import com.healthcare.backend.repository.UserRepository;

import com.healthcare.backend.service.MedicalReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalReportServiceImpl implements MedicalReportService {

    @Autowired
    private MedicalReportRepository medicalReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;


    @Override
    public MedicalReport saveReport(MedicalReport report) {

        // Get logged-in user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        // Report must have an appointment
        if (report.getAppointment() == null ||
                report.getAppointment().getId() == null) {

            throw new RuntimeException(
                    "Appointment ID is required");
        }


        // If ADMIN → can add report
        if (user.getRole().name().equals("ADMIN")) {

            return medicalReportRepository.save(report);
        }


        // If DOCTOR → report must belong to that doctor
        if (user.getRole().name().equals("DOCTOR")) {

            Doctor doctor = doctorRepository
                    .findByUserId(user.getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Doctor record not found"));

            Appointment appointment = report.getAppointment();

            if (appointment.getDoctor() == null ||
                    !appointment.getDoctor()
                            .getId()
                            .equals(doctor.getId())) {

                throw new AccessDeniedException(
                        "You can add reports only for your own appointments");
            }

            return medicalReportRepository.save(report);
        }


        throw new AccessDeniedException(
                "Only ADMIN or DOCTOR can add medical reports");
    }


    @Override
    public List<MedicalReport> getAllReports() {

        return medicalReportRepository.findAll();
    }


    @Override
    public MedicalReport getReportById(Long id) {

        MedicalReport report = medicalReportRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Medical report not found"));


        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        String role = user.getRole().name();


        // ADMIN can access any report
        if (role.equals("ADMIN")) {
            return report;
        }


        // Make sure report has appointment
        Appointment appointment =
                report.getAppointment();

        if (appointment == null) {

            throw new RuntimeException(
                    "Report is not linked to an appointment");
        }


        // PATIENT → only their own report
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
                        "You can access only your own medical reports");
            }

            return report;
        }


        // DOCTOR → only reports from their appointments
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
                        "You can access only reports from your appointments");
            }

            return report;
        }


        throw new AccessDeniedException(
                "Access denied");
    }


    @Override
    public void deleteReport(Long id) {

        medicalReportRepository.deleteById(id);
    }
}