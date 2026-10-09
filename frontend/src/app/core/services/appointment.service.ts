import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

export interface Appointment {
  id: number;
  patientId: number;
  patientName: string;
  doctorName: string;
  appointmentDate: string;
  appointmentTime: string;
  status: 'BOOKED' | 'COMPLETED';
}

export interface AppointmentRequest {
  patientId: number;
  doctorName: string;
  appointmentDate: string;
  appointmentTime: string;
}

@Injectable({ providedIn: 'root' })
export class AppointmentService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/appointments';

  list(todayOnly = false): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(todayOnly ? `${this.apiUrl}/today` : this.apiUrl);
  }

  book(appointment: AppointmentRequest): Observable<Appointment> { return this.http.post<Appointment>(this.apiUrl, appointment); }
}