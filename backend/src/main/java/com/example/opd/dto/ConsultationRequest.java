package com.example.opd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ConsultationRequest(
        @NotNull Long appointmentId,
        @NotBlank @Size(max = 500) String diagnosis,
        @Size(max = 2000) String notes) {
}