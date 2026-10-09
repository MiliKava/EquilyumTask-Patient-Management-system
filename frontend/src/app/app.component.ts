import { Component } from '@angular/core';
import { AppointmentsComponent } from './features/appointments/appointments.component';
import { ConsultationsComponent } from './features/consultations/consultations.component';
import { PatientsComponent } from './features/patients/patients.component';

type Section = 'patients' | 'appointments' | 'consultations';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [PatientsComponent, AppointmentsComponent, ConsultationsComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  activeSection: Section = 'patients';
  readonly today = new Date().toLocaleDateString('en-US', {
    weekday: 'long', month: 'long', day: 'numeric', year: 'numeric'
  });

  selectSection(section: Section): void {
    this.activeSection = section;
  }
}
