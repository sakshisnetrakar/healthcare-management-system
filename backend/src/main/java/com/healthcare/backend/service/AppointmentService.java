package com.healthcare.backend.service;

import com.healthcare.backend.entity.Appointment;

import java.util.List;

public interface AppointmentService {

    Appointment bookAppointment(Appointment appointment);

    List<Appointment> getAllAppointments();

    Appointment getAppointmentById(Long id);

    void deleteAppointment(Long id);
}