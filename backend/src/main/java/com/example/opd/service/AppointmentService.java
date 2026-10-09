package com.example.opd.service;

import com.example.opd.dto.AppointmentRequest;
import com.example.opd.dto.AppointmentResponse;
import com.example.opd.entity.Appointment;
import com.example.opd.entity.Patient;
import com.example.opd.exception.ResourceNotFoundException;
import com.example.opd.repository.AppointmentRepository;
import com.example.opd.repository.PatientRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    @Transactional
    public AppointmentResponse book(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient " + request.patientId() + " was not found."));
        Appointment appointment = new Appointment(patient, request.doctorName().trim(), request.appointmentDate(),
                request.appointmentTime());
        return toResponse(appointmentRepository.save(appointment));
    }

    public List<AppointmentResponse> findAll() {
        return appointmentRepository.findAllByOrderByAppointmentDateAscAppointmentTimeAsc()
                .stream().map(AppointmentService::toResponse).toList();
    }

    public List<AppointmentResponse> findToday() {
        return appointmentRepository.findByAppointmentDateOrderByAppointmentTimeAsc(LocalDate.now())
                .stream().map(AppointmentService::toResponse).toList();
    }

    public static AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getPatient().getId(),
                appointment.getPatient().getName(),
                appointment.getDoctorName(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getStatus());
    }
}