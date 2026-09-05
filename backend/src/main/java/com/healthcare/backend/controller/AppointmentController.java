package com.healthcare.backend.controller;

import com.healthcare.backend.dto.request.AppointmentRequestDTO;
import com.healthcare.backend.dto.response.AppointmentResponseDTO;
import com.healthcare.backend.service.AppointmentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;


    // Only PATIENT can book
    @PreAuthorize("hasRole(\"PATIENT\")")
    @PostMapping
    public AppointmentResponseDTO bookAppointment(
            @Valid @RequestBody AppointmentRequestDTO dto) {

        return appointmentService.bookAppointment(dto);
    }


    // Only ADMIN can view all
    @PreAuthorize("hasRole(\"ADMIN\")")
    @GetMapping
    public List<AppointmentResponseDTO> getAllAppointments() {

        return appointmentService.getAllAppointments();
    }


    // ADMIN, DOCTOR and PATIENT
    @PreAuthorize(
            "hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @GetMapping("/{id}")
    public AppointmentResponseDTO getAppointmentById(
            @PathVariable Long id) {

        return appointmentService.getAppointmentById(id);
    }


    // Only ADMIN can delete
    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("/{id}")
    public String deleteAppointment(
            @PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return "Appointment deleted successfully";
    }
}