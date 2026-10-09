package com.example.opd.service;

import com.example.opd.dto.ConsultationRequest;
import com.example.opd.dto.ConsultationResponse;
import com.example.opd.dto.ConsultationSaveResult;
import com.example.opd.entity.Appointment;
import com.example.opd.entity.Consultation;
import com.example.opd.entity.ConsultationStatus;
import com.example.opd.exception.ConflictException;
import com.example.opd.exception.ResourceNotFoundException;
import com.example.opd.repository.AppointmentRepository;
import com.example.opd.repository.ConsultationRepository;
import com.example.opd.repository.PatientRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public ConsultationService(
            ConsultationRepository consultationRepository,
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository) {
        this.consultationRepository = consultationRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    @Transactional
    public ConsultationSaveResult save(ConsultationRequest request) {
        Appointment appointment = appointmentRepository.findById(request.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment " + request.appointmentId() + " was not found."));
        Consultation consultation = consultationRepository.findByAppointment_Id(request.appointmentId()).orElse(null);
        boolean created = consultation == null;
        if (created) {
            consultation = new Consultation(appointment, request.diagnosis().trim(), normalizeNotes(request.notes()));
        } else {
            if (consultation.getStatus() == ConsultationStatus.COMPLETED) {
                throw new ConflictException("A completed consultation cannot be changed.");
            }
            consultation.updateDetails(request.diagnosis().trim(), normalizeNotes(request.notes()));
        }
        return new ConsultationSaveResult(toResponse(consultationRepository.save(consultation)), created);
    }

    @Transactional
    public ConsultationResponse complete(Long consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation " + consultationId + " was not found."));
        consultation.markCompleted();
        return toResponse(consultation);
    }

    public List<ConsultationResponse> findCompletedForPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient " + patientId + " was not found.");
        }
        return consultationRepository.findForPatientByStatus(patientId, ConsultationStatus.COMPLETED)
                .stream().map(ConsultationService::toResponse).toList();
    }

    private static String normalizeNotes(String notes) {
        return notes == null || notes.isBlank() ? null : notes.trim();
    }

    private static ConsultationResponse toResponse(Consultation consultation) {
        Appointment appointment = consultation.getAppointment();
        return new ConsultationResponse(
                consultation.getId(),
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getPatient().getName(),
                appointment.getDoctorName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                consultation.getDiagnosis(),
                consultation.getNotes(),
                consultation.getStatus());
    }
}