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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentServiceImpl
        implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;


    // =====================================================
    // BOOK APPOINTMENT
    // =====================================================

    @Override
    public AppointmentResponseDTO bookAppointment(
            AppointmentRequestDTO dto) {

        LocalDate appointmentDate =
                dto.getAppointmentDate();

        LocalTime appointmentTime =
                dto.getAppointmentTime();

        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();


        // Check date
        if (appointmentDate.isBefore(today)) {

            throw new IllegalArgumentException(
                    "Appointment date cannot be in the past");
        }


        // Check time if appointment is today
        if (appointmentDate.equals(today)
                && appointmentTime.isBefore(currentTime)) {

            throw new IllegalArgumentException(
                    "Appointment time cannot be in the past");
        }


        // Get logged-in user
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


        // Check active user
        if (!Boolean.TRUE.equals(user.getActive())) {

            throw new AccessDeniedException(
                    "Inactive users cannot book appointments");
        }


        // Find patient
        Patient patient =
                patientRepository
                        .findByUserId(user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Patient record not found"));


        // Find doctor
        Doctor doctor =
                doctorRepository
                        .findById(dto.getDoctorId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found"));


        // Check doctor availability
        boolean alreadyBooked =
                appointmentRepository
                        .existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                                doctor.getId(),
                                appointmentDate,
                                appointmentTime
                        );


        if (alreadyBooked) {

            throw new IllegalArgumentException(
                    "Doctor is already booked for this date and time");
        }


        // Create appointment
        Appointment appointment =
                new Appointment();

        appointment.setAppointmentDate(
                appointmentDate);

        appointment.setAppointmentTime(
                appointmentTime);

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


    // =====================================================
    // GET ALL APPOINTMENTS
    // =====================================================

    @Override
    public List<AppointmentResponseDTO>
    getAllAppointments() {

        return appointmentRepository.findAll()
                .stream()
                .map(AppointmentMapper::toResponseDTO)
                .toList();
    }


    // =====================================================
    // GET APPOINTMENT BY ID
    // =====================================================

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


        // ADMIN
        if (role.equals("ADMIN")) {

            return AppointmentMapper.toResponseDTO(
                    appointment);
        }


        // PATIENT
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


        // DOCTOR
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


    // =====================================================
    // COMPLETE APPOINTMENT
    // =====================================================

    @Override
    public AppointmentResponseDTO
    completeAppointment(Long id) {

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


        // Only the doctor assigned to this
        // appointment can complete it
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
                    "You can complete only your own appointments");
        }


        // Appointment must be BOOKED
        if (appointment.getStatus()
                != AppointmentStatus.BOOKED) {

            throw new IllegalArgumentException(
                    "Only booked appointments can be completed");
        }


        appointment.setStatus(
                AppointmentStatus.COMPLETED);


        Appointment updatedAppointment =
                appointmentRepository.save(
                        appointment);


        return AppointmentMapper.toResponseDTO(
                updatedAppointment);
    }


    // =====================================================
    // CANCEL APPOINTMENT
    // =====================================================

    @Override
    public AppointmentResponseDTO
    cancelAppointment(Long id) {

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


        // ADMIN can cancel any appointment
        if (role.equals("ADMIN")) {

            appointment.setStatus(
                    AppointmentStatus.CANCELLED);

            Appointment updatedAppointment =
                    appointmentRepository.save(
                            appointment);

            return AppointmentMapper.toResponseDTO(
                    updatedAppointment);
        }


        // PATIENT can cancel only their own appointment
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
                        "You can cancel only your own appointments");
            }


            if (appointment.getStatus()
                    != AppointmentStatus.BOOKED) {

                throw new IllegalArgumentException(
                        "Only booked appointments can be cancelled");
            }


            appointment.setStatus(
                    AppointmentStatus.CANCELLED);

            Appointment updatedAppointment =
                    appointmentRepository.save(
                            appointment);

            return AppointmentMapper.toResponseDTO(
                    updatedAppointment);
        }


        // DOCTOR can cancel their own appointment
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
                        "You can cancel only your own appointments");
            }


            if (appointment.getStatus()
                    != AppointmentStatus.BOOKED) {

                throw new IllegalArgumentException(
                        "Only booked appointments can be cancelled");
            }


            appointment.setStatus(
                    AppointmentStatus.CANCELLED);

            Appointment updatedAppointment =
                    appointmentRepository.save(
                            appointment);

            return AppointmentMapper.toResponseDTO(
                    updatedAppointment);
        }


        throw new AccessDeniedException(
                "Access denied");
    }


    // =====================================================
    // DELETE APPOINTMENT
    // =====================================================

    @Override
    public void deleteAppointment(Long id) {

        if (!appointmentRepository.existsById(id)) {

            throw new ResourceNotFoundException(
                    "Appointment not found");
        }

        appointmentRepository.deleteById(id);
    }
}