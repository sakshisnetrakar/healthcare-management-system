package com.healthcare.backend.mapper;

import com.healthcare.backend.dto.request.MedicalReportRequestDTO;
import com.healthcare.backend.dto.response.MedicalReportResponseDTO;
import com.healthcare.backend.entity.MedicalReport;

public class MedicalReportMapper {

    public static MedicalReport toEntity(
            MedicalReportRequestDTO dto) {

        MedicalReport report = new MedicalReport();

        report.setReportName(dto.getReportName());
        report.setReportType(dto.getReportType());
        report.setFindings(dto.getFindings());

        return report;
    }


    public static MedicalReportResponseDTO toResponseDTO(
            MedicalReport report) {

        MedicalReportResponseDTO dto =
                new MedicalReportResponseDTO();

        dto.setId(report.getId());
        dto.setReportName(report.getReportName());
        dto.setReportType(report.getReportType());
        dto.setFindings(report.getFindings());

        if (report.getAppointment() != null) {

            dto.setAppointmentId(
                    report.getAppointment().getId());

            if (report.getAppointment().getPatient() != null) {

                dto.setPatientId(
                        report.getAppointment()
                                .getPatient()
                                .getId());

                dto.setPatientName(
                        report.getAppointment()
                                .getPatient()
                                .getPatientName());
            }

            if (report.getAppointment().getDoctor() != null) {

                dto.setDoctorId(
                        report.getAppointment()
                                .getDoctor()
                                .getId());

                dto.setDoctorName(
                        report.getAppointment()
                                .getDoctor()
                                .getDoctorName());
            }
        }

        return dto;
    }
}