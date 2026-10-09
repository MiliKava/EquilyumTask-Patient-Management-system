package com.example.opd.service;

import com.example.opd.dto.PatientRequest;
import com.example.opd.dto.PatientResponse;
import com.example.opd.entity.Patient;
import com.example.opd.repository.PatientRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Transactional
    public PatientResponse create(PatientRequest request) {
        Patient patient = new Patient(request.name().trim(), request.age(), request.gender().trim(),
                request.phone().trim());
        return toResponse(patientRepository.save(patient));
    }

    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream().map(PatientService::toResponse).toList();
    }

    public List<PatientResponse> search(String keyword) {
        String value = keyword == null ? "" : keyword.trim();
        return patientRepository.findByNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(value, value)
                .stream().map(PatientService::toResponse).toList();
    }

    public static PatientResponse toResponse(Patient patient) {
        return new PatientResponse(patient.getId(), patient.getName(), patient.getAge(), patient.getGender(),
                patient.getPhone());
    }
}