package com.healthcare.backend.service.impl;

import com.healthcare.backend.entity.MedicalReport;
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

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        // ADMIN can add any report
        if (user.getRole().name().equals("ADMIN")) {
            return medicalReportRepository.save(report);
        }


        // Only DOCTOR reaches here because controller
        // already restricts POST to DOCTOR and ADMIN
        if (user.getRole().name().equals("DOCTOR")) {

            Doctor doctor = doctorRepository
                    .findByUserId(user.getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Doctor record not found"));

            if (report.getAppointment() == null ||
                    report.getAppointment().getId() == null) {

                throw new RuntimeException(
                        "Appointment ID is required");
            }

            // Get appointment's doctor
            Long appointmentDoctorId =
                    report.getAppointment()
                            .getDoctor()
                            .getId();

            // Doctor can create report only
            // for their own appointment
            if (!doctor.getId().equals(appointmentDoctorId)) {

                throw new AccessDeniedException(
                        "You can create reports only for your own appointments");
            }
        }

        return medicalReportRepository.save(report);
    }


    @Override
    public List<MedicalReport> getAllReports() {

        return medicalReportRepository.findAll();
    }


    @Override
    public MedicalReport getReportById(Long id) {

        MedicalReport report =
                medicalReportRepository.findById(id)
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


        // ADMIN can access any report
        if (user.getRole().name().equals("ADMIN")) {
            return report;
        }


        // Make sure report has an appointment
        if (report.getAppointment() == null) {
            throw new RuntimeException(
                    "Report is not linked to an appointment");
        }


        // PATIENT
        if (user.getRole().name().equals("PATIENT")) {

            Patient patient = patientRepository
                    .findByUserId(user.getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Patient record not found"));

            Long reportPatientId =
                    report.getAppointment()
                            .getPatient()
                            .getId();

            if (!patient.getId().equals(reportPatientId)) {

                throw new AccessDeniedException(
                        "You can access only your own medical reports");
            }

            return report;
        }


        // DOCTOR
        if (user.getRole().name().equals("DOCTOR")) {

            Doctor doctor = doctorRepository
                    .findByUserId(user.getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Doctor record not found"));

            Long reportDoctorId =
                    report.getAppointment()
                            .getDoctor()
                            .getId();

            if (!doctor.getId().equals(reportDoctorId)) {

                throw new AccessDeniedException(
                        "You can access only reports related to your appointments");
            }

            return report;
        }


        throw new AccessDeniedException("Access denied");
    }


    @Override
    public void deleteReport(Long id) {

        medicalReportRepository.deleteById(id);
    }
}