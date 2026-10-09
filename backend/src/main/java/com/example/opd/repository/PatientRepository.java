package com.example.opd.repository;

import com.example.opd.entity.Patient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(String name, String phone);
}