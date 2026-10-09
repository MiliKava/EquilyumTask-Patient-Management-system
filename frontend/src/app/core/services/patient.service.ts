import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

export interface Patient {
  id: number;
  name: string;
  age: number;
  gender: string;
  phone: string;
}

export interface PatientRequest {
  name: string;
  age: number;
  gender: string;
  phone: string;
}

@Injectable({ providedIn: 'root' })
export class PatientService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/patients';

  list(): Observable<Patient[]> { return this.http.get<Patient[]>(this.apiUrl); }

  search(keyword: string): Observable<Patient[]> {
    const params = new HttpParams().set('keyword', keyword);
    return this.http.get<Patient[]>(`${this.apiUrl}/search`, { params });
  }

  create(patient: PatientRequest): Observable<Patient> { return this.http.post<Patient>(this.apiUrl, patient); }
}