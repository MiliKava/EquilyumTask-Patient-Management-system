package com.example.opd.repository;

import com.example.opd.entity.Consultation;
import com.example.opd.entity.ConsultationStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    Optional<Consultation> findByAppointment_Id(Long appointmentId);

    @Query("select c from Consultation c where c.appointment.patient.id = :patientId "
            + "and c.status = :status order by c.appointment.appointmentDate desc, c.appointment.appointmentTime desc")
    List<Consultation> findForPatientByStatus(
            @Param("patientId") Long patientId,
            @Param("status") ConsultationStatus status);
}