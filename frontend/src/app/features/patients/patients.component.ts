import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { getApiError } from '../../core/services/api-error';
import { Patient, PatientService } from '../../core/services/patient.service';

@Component({
  selector: 'app-patients', standalone: true, imports: [CommonModule, FormsModule],
  templateUrl: './patients.component.html', styleUrl: './patients.component.css'
})
export class PatientsComponent implements OnInit {
  private readonly patientService = inject(PatientService);
  patients: Patient[] = [];
  form = { name: '', age: null as number | null, gender: '', phone: '' };
  keyword = '';
  loading = true;
  saving = false;
  message = '';
  error = '';

  ngOnInit(): void { this.loadPatients(); }

  loadPatients(): void {
    this.loading = true;
    this.patientService.list().subscribe({
      next: (patients) => { this.patients = patients; this.loading = false; },
      error: (error: unknown) => { this.error = getApiError(error, 'Could not load patients.'); this.loading = false; }
    });
  }

  searchPatients(): void {
    const keyword = this.keyword.trim();
    if (!keyword) { this.loadPatients(); return; }
    this.loading = true;
    this.error = '';
    this.patientService.search(keyword).subscribe({
      next: (patients) => { this.patients = patients; this.loading = false; },
      error: (error: unknown) => { this.error = getApiError(error, 'Could not search patients.'); this.loading = false; }
    });
  }

  registerPatient(): void {
    if (this.form.age === null) return;
    this.saving = true;
    this.error = '';
    this.message = '';
    this.patientService.create({ ...this.form, age: this.form.age }).subscribe({
      next: () => {
        this.message = 'Patient registered.';
        this.form = { name: '', age: null, gender: '', phone: '' };
        this.keyword = '';
        this.saving = false;
        this.loadPatients();
      },
      error: (error: unknown) => { this.error = getApiError(error, 'Could not register patient.'); this.saving = false; }
    });
  }
}