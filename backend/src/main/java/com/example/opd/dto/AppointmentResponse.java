package com.example.opd.dto;

import com.example.opd.entity.AppointmentStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentResponse(
        Long id,
        Long patientId,
        String patientName,
        String doctorName,
        LocalDate appointmentDate,
        LocalTime appointmentTime,
        AppointmentStatus status) {
}