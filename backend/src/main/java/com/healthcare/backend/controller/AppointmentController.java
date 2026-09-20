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


    // =====================================================
    // BOOK
    // =====================================================

    @PreAuthorize("hasRole(\"PATIENT\")")
    @PostMapping
    public AppointmentResponseDTO bookAppointment(
            @Valid @RequestBody AppointmentRequestDTO dto) {

        return appointmentService.bookAppointment(dto);
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @PreAuthorize("hasRole(\"ADMIN\")")
    @GetMapping
    public List<AppointmentResponseDTO>
    getAllAppointments() {

        return appointmentService
                .getAllAppointments();
    }


    // =====================================================
    // GET BY ID
    // =====================================================

    @PreAuthorize(
            "hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @GetMapping("/{id}")
    public AppointmentResponseDTO getAppointmentById(
            @PathVariable Long id) {

        return appointmentService
                .getAppointmentById(id);
    }


    // =====================================================
    // COMPLETE
    // =====================================================

    @PreAuthorize("hasRole(\"DOCTOR\")")
    @PutMapping("/{id}/complete")
    public AppointmentResponseDTO completeAppointment(
            @PathVariable Long id) {

        return appointmentService
                .completeAppointment(id);
    }


    // =====================================================
    // CANCEL
    // =====================================================

    @PreAuthorize(
            "hasAnyRole(\"ADMIN\", \"DOCTOR\", \"PATIENT\")")
    @PutMapping("/{id}/cancel")
    public AppointmentResponseDTO cancelAppointment(
            @PathVariable Long id) {

        return appointmentService
                .cancelAppointment(id);
    }


    // =====================================================
    // DELETE
    // =====================================================

    @PreAuthorize("hasRole(\"ADMIN\")")
    @DeleteMapping("/{id}")
    public String deleteAppointment(
            @PathVariable Long id) {

        appointmentService
                .deleteAppointment(id);

        return "Appointment deleted successfully";
    }
}