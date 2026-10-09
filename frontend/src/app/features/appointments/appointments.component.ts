import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Appointment, AppointmentService } from '../../core/services/appointment.service';
import { getApiError } from '../../core/services/api-error';
import { Patient, PatientService } from '../../core/services/patient.service';

@Component({
  selector: 'app-appointments', standalone: true, imports: [CommonModule, FormsModule],
  templateUrl: './appointments.component.html', styleUrl: './appointments.component.css'
})
export class AppointmentsComponent implements OnInit {
  private readonly appointmentService = inject(AppointmentService);
  private readonly patientService = inject(PatientService);
  patients: Patient[] = [];
  appointments: Appointment[] = [];
  form = { patientId: null as number | null, doctorName: '', appointmentDate: this.localDate(), appointmentTime: '09:00' };
  todayOnly = false;
  loading = true;
  saving = false;
  message = '';
  error = '';

  ngOnInit(): void {
    this.patientService.list().subscribe({
      next: (patients) => { this.patients = patients; },
      error: (error: unknown) => { this.error = getApiError(error, 'Could not load patients.'); }
    });
    this.loadAppointments();
  }

  loadAppointments(): void {
    this.loading = true;
    this.appointmentService.list(this.todayOnly).subscribe({
      next: (appointments) => { this.appointments = appointments; this.loading = false; },
      error: (error: unknown) => { this.error = getApiError(error, 'Could not load appointments.'); this.loading = false; }
    });
  }

  setTodayOnly(todayOnly: boolean): void { this.todayOnly = todayOnly; this.loadAppointments(); }

  bookAppointment(): void {
    if (this.form.patientId === null) return;
    this.saving = true;
    this.error = '';
    this.message = '';
    this.appointmentService.book({ ...this.form, patientId: this.form.patientId }).subscribe({
      next: () => {
        this.message = 'Appointment booked.';
        this.form = { patientId: null, doctorName: '', appointmentDate: this.localDate(), appointmentTime: '09:00' };
        this.saving = false;
        this.loadAppointments();
      },
      error: (error: unknown) => { this.error = getApiError(error, 'Could not book appointment.'); this.saving = false; }
    });
  }

  private localDate(): string {
    const date = new Date();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${date.getFullYear()}-${month}-${day}`;
  }
}