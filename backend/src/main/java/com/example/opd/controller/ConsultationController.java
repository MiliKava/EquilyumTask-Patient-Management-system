package com.example.opd.controller;

import com.example.opd.dto.ConsultationRequest;
import com.example.opd.dto.ConsultationResponse;
import com.example.opd.dto.ConsultationSaveResult;
import com.example.opd.service.ConsultationService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consultations")
@CrossOrigin(origins = "http://localhost:4200")
public class ConsultationController {

    private final ConsultationService consultationService;

    public ConsultationController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @PostMapping
    public ResponseEntity<ConsultationResponse> save(@Valid @RequestBody ConsultationRequest request) {
        ConsultationSaveResult result = consultationService.save(request);
        HttpStatus status = result.created() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(result.consultation());
    }

    @PutMapping("/{id}/complete")
    public ConsultationResponse complete(@PathVariable Long id) {
        return consultationService.complete(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<ConsultationResponse> findCompletedForPatient(@PathVariable Long patientId) {
        return consultationService.findCompletedForPatient(patientId);
    }
}