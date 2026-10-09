package com.example.opd.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PatientRequest(
        @NotBlank @Size(max = 100) String name,
        @NotNull @Min(1) @Max(120) Integer age,
        @NotBlank @Size(max = 30) String gender,
        @NotBlank @Pattern(regexp = "^[0-9+() -]{7,20}$", message = "must be a valid phone number") String phone) {
}