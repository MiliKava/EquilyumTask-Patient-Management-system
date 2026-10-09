package com.example.opd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequest(
        @NotNull Long patientId,
        @NotBlank @Size(max = 100) String doctorName,
        @NotNull LocalDate appointmentDate,
        @NotNull LocalTime appointmentTime) {
}