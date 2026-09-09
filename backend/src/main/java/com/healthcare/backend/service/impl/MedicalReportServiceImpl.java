package com.healthcare.backend.service.impl;

import com.healthcare.backend.dto.request.MedicalReportRequestDTO;
import com.healthcare.backend.dto.response.MedicalReportResponseDTO;
import com.healthcare.backend.entity.Appointment;
import com.healthcare.backend.entity.Doctor;
import com.healthcare.backend.entity.MedicalReport;
import com.healthcare.backend.entity.Patient;
import com.healthcare.backend.entity.User;
import com.healthcare.backend.exception.ResourceNotFoundException;
import com.healthcare.backend.mapper.MedicalReportMapper;
import com.healthcare.backend.repository.AppointmentRepository;
import com.healthcare.backend.repository.DoctorRepository;
import com.healthcare.backend.repository.MedicalReportRepository;
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
public class MedicalReportServiceImpl
        implements MedicalReportService {

    @Autowired
    private MedicalReportRepository medicalReportRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;


    @Override
    public MedicalReportResponseDTO addMedicalReport(
            MedicalReportRequestDTO dto) {

        Appointment appointment =
                appointmentRepository
                        .findById(dto.getAppointmentId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found"));

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        String role = user.getRole().name();


        if (role.equals("ADMIN")) {

            MedicalReport report =
                    MedicalReportMapper.toEntity(dto);

            report.setAppointment(appointment);

            MedicalReport savedReport =
                    medicalReportRepository.save(report);

            return MedicalReportMapper.toResponseDTO(
                    savedReport);
        }


        if (role.equals("DOCTOR")) {

            Doctor doctor =
                    doctorRepository
                            .findByUserId(user.getId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Doctor record not found"));

            if (!appointment.getDoctor()
                    .getId()
                    .equals(doctor.getId())) {

                throw new AccessDeniedException(
                        "You can create reports only for your own appointments");
            }

            MedicalReport report =
                    MedicalReportMapper.toEntity(dto);

            report.setAppointment(appointment);

            MedicalReport savedReport =
                    medicalReportRepository.save(report);

            return MedicalReportMapper.toResponseDTO(
                    savedReport);
        }


        throw new AccessDeniedException(
                "You are not allowed to create medical reports");
    }


    @Override
    public List<MedicalReportResponseDTO>
    getAllMedicalReports() {

        return medicalReportRepository.findAll()
                .stream()
                .map(MedicalReportMapper::toResponseDTO)
                .toList();
    }


    @Override
    public MedicalReportResponseDTO
    getMedicalReportById(Long id) {

        MedicalReport report =
                medicalReportRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Medical report not found"));

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        String role = user.getRole().name();


        if (role.equals("ADMIN")) {

            return MedicalReportMapper.toResponseDTO(
                    report);
        }


        if (role.equals("PATIENT")) {

            Patient patient =
                    patientRepository
                            .findByUserId(user.getId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Patient record not found"));

            if (!report.getAppointment()
                    .getPatient()
                    .getId()
                    .equals(patient.getId())) {

                throw new AccessDeniedException(
                        "You can access only your own medical reports");
            }

            return MedicalReportMapper.toResponseDTO(
                    report);
        }


        if (role.equals("DOCTOR")) {

            Doctor doctor =
                    doctorRepository
                            .findByUserId(user.getId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Doctor record not found"));

            if (!report.getAppointment()
                    .getDoctor()
                    .getId()
                    .equals(doctor.getId())) {

                throw new AccessDeniedException(
                        "You can access only reports for your appointments");
            }

            return MedicalReportMapper.toResponseDTO(
                    report);
        }


        throw new AccessDeniedException(
                "Access denied");
    }


    @Override
    public void deleteMedicalReport(Long id) {

        if (!medicalReportRepository.existsById(id)) {

            throw new ResourceNotFoundException(
                    "Medical report not found");
        }

        medicalReportRepository.deleteById(id);
    }
}