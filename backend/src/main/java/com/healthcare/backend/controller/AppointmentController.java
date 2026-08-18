package com.healthcare.backend.controller;

import com.healthcare.backend.entity.Appointment;
import com.healthcare.backend.service.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Only PATIENT can book an appointment
    @PreAuthorize("hasRole(\"PATIENT\")")
    @PostMapping
    public Appointment bookAppointment(
            @RequestBody Appointment appointment) {

        return appointmentService.bookAppointment(appointment);
    }

    // Only ADMIN can view all appointments
    @PreAuthorize("hasRole(\"ADMIN\")")
    @GetMapping
    public List<Appointment> getAllAppointments() {

        return appointmentService.getAllAppointments();
    }

    // ADMIN, DOCTOR and PATIENT can access this for now
    // Ownership checks will be added next
    @PreAuthorize("hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @GetMapping("/{id}")
    public Appointment getAppointmentById(
            @PathVariable Long id) {

        return appointmentService.getAppointmentById(id);
    }

    // Only ADMIN can delete appointments
    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("/{id}")
    public String deleteAppointment(
            @PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return "Appointment deleted successfully";
    }
}