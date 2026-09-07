package com.healthcare.backend.service.impl;

import com.healthcare.backend.dto.request.PrescriptionRequestDTO;
import com.healthcare.backend.dto.response.PrescriptionResponseDTO;
import com.healthcare.backend.entity.Appointment;
import com.healthcare.backend.entity.Doctor;
import com.healthcare.backend.entity.Patient;
import com.healthcare.backend.entity.Prescription;
import com.healthcare.backend.entity.User;
import com.healthcare.backend.mapper.PrescriptionMapper;
import com.healthcare.backend.repository.AppointmentRepository;
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
public class PrescriptionServiceImpl
        implements PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;


    @Override
    public PrescriptionResponseDTO addPrescription(
            PrescriptionRequestDTO dto) {

        Appointment appointment =
                appointmentRepository
                        .findById(dto.getAppointmentId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment not found"));


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


        // ADMIN can create for any appointment
        if (role.equals("ADMIN")) {

            Prescription prescription =
                    PrescriptionMapper.toEntity(dto);

            prescription.setAppointment(appointment);

            Prescription savedPrescription =
                    prescriptionRepository
                            .save(prescription);

            return PrescriptionMapper.toResponseDTO(
                    savedPrescription);
        }


        // DOCTOR can create only for their appointment
        if (role.equals("DOCTOR")) {

            Doctor doctor =
                    doctorRepository
                            .findByUserId(user.getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Doctor record not found"));

            if (!appointment.getDoctor()
                    .getId()
                    .equals(doctor.getId())) {

                throw new AccessDeniedException(
                        "You can create prescriptions only for your own appointments");
            }

            Prescription prescription =
                    PrescriptionMapper.toEntity(dto);

            prescription.setAppointment(appointment);

            Prescription savedPrescription =
                    prescriptionRepository
                            .save(prescription);

            return PrescriptionMapper.toResponseDTO(
                    savedPrescription);
        }


        throw new AccessDeniedException(
                "You are not allowed to create prescriptions");
    }


    @Override
    public List<PrescriptionResponseDTO>
    getAllPrescriptions() {

        return prescriptionRepository.findAll()
                .stream()
                .map(PrescriptionMapper::toResponseDTO)
                .toList();
    }


    @Override
    public PrescriptionResponseDTO
    getPrescriptionById(Long id) {

        Prescription prescription =
                prescriptionRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Prescription not found"));


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


        // ADMIN
        if (role.equals("ADMIN")) {

            return PrescriptionMapper.toResponseDTO(
                    prescription);
        }


        // PATIENT
        if (role.equals("PATIENT")) {

            Patient patient =
                    patientRepository
                            .findByUserId(user.getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Patient record not found"));

            if (!prescription.getAppointment()
                    .getPatient()
                    .getId()
                    .equals(patient.getId())) {

                throw new AccessDeniedException(
                        "You can access only your own prescriptions");
            }

            return PrescriptionMapper.toResponseDTO(
                    prescription);
        }


        // DOCTOR
        if (role.equals("DOCTOR")) {

            Doctor doctor =
                    doctorRepository
                            .findByUserId(user.getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Doctor record not found"));

            if (!prescription.getAppointment()
                    .getDoctor()
                    .getId()
                    .equals(doctor.getId())) {

                throw new AccessDeniedException(
                        "You can access only prescriptions for your appointments");
            }

            return PrescriptionMapper.toResponseDTO(
                    prescription);
        }


        throw new AccessDeniedException(
                "Access denied");
    }


    @Override
    public void deletePrescription(Long id) {

        if (!prescriptionRepository.existsById(id)) {

            throw new RuntimeException(
                    "Prescription not found");
        }

        prescriptionRepository.deleteById(id);
    }
}