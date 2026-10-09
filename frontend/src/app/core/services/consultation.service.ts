import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

export interface Consultation {
  id: number;
  appointmentId: number;
  patientId: number;
  patientName: string;
  doctorName: string;
  appointmentDate: string;
  appointmentTime: string;
  diagnosis: string;
  notes: string | null;
  status: 'INCOMPLETE' | 'COMPLETED';
}

export interface ConsultationRequest {
  appointmentId: number;
  diagnosis: string;
  notes: string;
}

@Injectable({ providedIn: 'root' })
export class ConsultationService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/consultations';

  save(consultation: ConsultationRequest): Observable<Consultation> { return this.http.post<Consultation>(this.apiUrl, consultation); }

  complete(consultationId: number): Observable<Consultation> {
    return this.http.put<Consultation>(`${this.apiUrl}/${consultationId}/complete`, {});
  }

  completedForPatient(patientId: number): Observable<Consultation[]> {
    return this.http.get<Consultation[]>(`${this.apiUrl}/patient/${patientId}`);
  }
}