import { CommonModule, DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ApplicationsService } from '../../../core/services/applications.service';
import { ApplicationResponse, ApplicationSubmitResponse } from '../../../core/models/application.models';

@Component({
  selector: 'app-application-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, DatePipe],
  templateUrl: './application-detail.component.html',
  styleUrls: ['./application-detail.component.css']
})
export class ApplicationDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private applicationsService = inject(ApplicationsService);

  applicationId!: number;
  application: ApplicationResponse | null = null;
  submissionResult: ApplicationSubmitResponse | null = null;

  isLoading = false;
  isValidating = false;
  isRejecting = false;
  isSubmitting = false;

  errorMessage = '';
  successMessage = '';

  ngOnInit(): void {
    this.applicationId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadApplication();
  }

  loadApplication(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.applicationsService.getById(this.applicationId).subscribe({
      next: (app) => {
        this.application = app;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err?.error?.error ?? 'Impossible de charger la candidature';
      }
    });
  }

  validate(): void {
    if (!this.application) return;

    this.isValidating = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.applicationsService.validate(this.applicationId).subscribe({
      next: () => {
        this.isValidating = false;
        this.successMessage = 'Candidature validée.';
        this.loadApplication();
      },
      error: (err) => {
        this.isValidating = false;
        this.errorMessage = err?.error?.error ?? 'Impossible de valider la candidature';
      }
    });
  }

  reject(): void {
    if (!this.application) return;

    this.isRejecting = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.applicationsService.reject(this.applicationId).subscribe({
      next: () => {
        this.isRejecting = false;
        this.successMessage = 'Candidature rejetée.';
        this.loadApplication();
      },
      error: (err) => {
        this.isRejecting = false;
        this.errorMessage = err?.error?.error ?? 'Impossible de rejeter la candidature';
      }
    });
  }

  submit(): void {
    if (!this.application) return;

    this.isSubmitting = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.applicationsService.submit(this.applicationId).subscribe({
      next: (result) => {
        this.submissionResult = result;
        this.isSubmitting = false;
        this.successMessage = 'Candidature soumise.';
        this.loadApplication();
      },
      error: (err) => {
        this.isSubmitting = false;
        this.errorMessage = err?.error?.error ?? 'Impossible de soumettre la candidature';
      }
    });
  }

  get canValidate(): boolean {
    return this.application?.status === 'GENERATED';
  }

  get canReject(): boolean {
    return this.application?.status === 'GENERATED';
  }

  get canSubmit(): boolean {
    return this.application?.status === 'VALIDATED';
  }
}