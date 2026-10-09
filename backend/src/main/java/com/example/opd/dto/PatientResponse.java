package com.example.opd.dto;

public record PatientResponse(Long id, String name, Integer age, String gender, String phone) {
}