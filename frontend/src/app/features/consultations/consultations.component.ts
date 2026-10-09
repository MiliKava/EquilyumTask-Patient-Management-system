import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Appointment, AppointmentService } from '../../core/services/appointment.service';
import { getApiError } from '../../core/services/api-error';
import { Consultation, ConsultationService } from '../../core/services/consultation.service';
import { Patient, PatientService } from '../../core/services/patient.service';

@Component({
  selector: 'app-consultations', standalone: true, imports: [CommonModule, FormsModule],
  templateUrl: './consultations.component.html', styleUrl: './consultations.component.css'
})
export class ConsultationsComponent implements OnInit {
  private readonly appointmentService = inject(AppointmentService);
  private readonly consultationService = inject(ConsultationService);
  private readonly patientService = inject(PatientService);
  patients: Patient[] = [];
  appointments: Appointment[] = [];
  completedConsultations: Consultation[] = [];
  selectedAppointmentId: number | null = null;
  selectedPatientId: number | null = null;
  savedConsultation: Consultation | null = null;
  form = { diagnosis: '', notes: '' };
  loading = true;
  saving = false;
  completing = false;
  message = '';
  error = '';

  ngOnInit(): void {
    this.patientService.list().subscribe({
      next: (patients) => { this.patients = patients; },
      error: (error: unknown) => { this.error = getApiError(error, 'Could not load patients.'); }
    });
    this.appointmentService.list().subscribe({
      next: (appointments) => { this.appointments = appointments; this.loading = false; },
      error: (error: unknown) => { this.error = getApiError(error, 'Could not load appointments.'); this.loading = false; }
    });
  }

  selectAppointment(appointmentId: number | null): void {
    this.selectedAppointmentId = appointmentId;
    this.savedConsultation = null;
    this.form = { diagnosis: '', notes: '' };
    this.message = '';
    this.error = '';
  }

  saveConsultation(): void {
    if (this.selectedAppointmentId === null) return;
    this.saving = true;
    this.message = '';
    this.error = '';
    this.consultationService.save({ ...this.form, appointmentId: this.selectedAppointmentId }).subscribe({
      next: (consultation) => { this.savedConsultation = consultation; this.message = 'Consultation saved.'; this.saving = false; },
      error: (error: unknown) => { this.error = getApiError(error, 'Could not save consultation.'); this.saving = false; }
    });
  }

  completeConsultation(): void {
    if (!this.savedConsultation) return;
    this.completing = true;
    this.error = '';
    this.consultationService.complete(this.savedConsultation.id).subscribe({
      next: (consultation) => {
        this.savedConsultation = consultation;
        this.message = 'Consultation completed.';
        this.completing = false;
        this.appointmentService.list().subscribe({ next: (appointments) => { this.appointments = appointments; } });
      },
      error: (error: unknown) => { this.error = getApiError(error, 'Could not complete consultation.'); this.completing = false; }
    });
  }

  loadCompletedConsultations(): void {
    if (this.selectedPatientId === null) { this.completedConsultations = []; return; }
    this.loading = true;
    this.consultationService.completedForPatient(this.selectedPatientId).subscribe({
      next: (consultations) => { this.completedConsultations = consultations; this.loading = false; },
      error: (error: unknown) => { this.error = getApiError(error, 'Could not load consultations.'); this.loading = false; }
    });
  }
}