package com.healthcare.backend.repository;

import com.healthcare.backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTime(
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime);

    List<Appointment> findByDoctorId(
            Long doctorId);

    List<Appointment> findByPatientId(
            Long patientId);
}