package com.healthcare.backend.service.impl;

import com.healthcare.backend.dto.request.AppointmentRequestDTO;
import com.healthcare.backend.dto.response.AppointmentResponseDTO;
import com.healthcare.backend.entity.Appointment;
import com.healthcare.backend.entity.Doctor;
import com.healthcare.backend.entity.Patient;
import com.healthcare.backend.entity.User;
import com.healthcare.backend.enums.AppointmentStatus;
import com.healthcare.backend.exception.ResourceNotFoundException;
import com.healthcare.backend.mapper.AppointmentMapper;

import com.healthcare.backend.repository.DoctorRepository;
import com.healthcare.backend.repository.PatientRepository;
import com.healthcare.backend.repository.AppointmentRepository;
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
    public AppointmentResponseDTO bookAppointment(
            AppointmentRequestDTO dto) {

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

        Patient patient = patientRepository
                .findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient record not found"));

        Doctor doctor = doctorRepository
                .findById(dto.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found"));

        Appointment appointment =
                new Appointment();

        appointment.setAppointmentDate(
                dto.getAppointmentDate());

        appointment.setAppointmentTime(
                dto.getAppointmentTime());

        appointment.setDoctor(doctor);

        appointment.setPatient(patient);

        appointment.setStatus(
                AppointmentStatus.BOOKED);

        Appointment savedAppointment =
                appointmentRepository.save(
                        appointment);

        return AppointmentMapper.toResponseDTO(
                savedAppointment);
    }


    @Override
    public List<AppointmentResponseDTO>
    getAllAppointments() {

        return appointmentRepository.findAll()
                .stream()
                .map(AppointmentMapper::toResponseDTO)
                .toList();
    }


    @Override
    public AppointmentResponseDTO
    getAppointmentById(Long id) {

        Appointment appointment =
                appointmentRepository
                        .findById(id)
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

            return AppointmentMapper.toResponseDTO(
                    appointment);
        }


        if (role.equals("PATIENT")) {

            Patient patient =
                    patientRepository
                            .findByUserId(user.getId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Patient record not found"));

            if (!appointment.getPatient()
                    .getId()
                    .equals(patient.getId())) {

                throw new AccessDeniedException(
                        "You can access only your own appointments");
            }

            return AppointmentMapper.toResponseDTO(
                    appointment);
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
                        "You can access only your own appointments");
            }

            return AppointmentMapper.toResponseDTO(
                    appointment);
        }


        throw new AccessDeniedException(
                "Access denied");
    }


    @Override
    public void deleteAppointment(Long id) {

        if (!appointmentRepository.existsById(id)) {

            throw new ResourceNotFoundException(
                    "Appointment not found");
        }

        appointmentRepository.deleteById(id);
    }
}