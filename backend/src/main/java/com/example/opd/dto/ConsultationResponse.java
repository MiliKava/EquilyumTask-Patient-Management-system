package com.example.opd.dto;

import com.example.opd.entity.ConsultationStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultationResponse(
        Long id,
        Long appointmentId,
        Long patientId,
        String patientName,
        String doctorName,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        String diagnosis,
        String notes,
        ConsultationStatus status) {
}