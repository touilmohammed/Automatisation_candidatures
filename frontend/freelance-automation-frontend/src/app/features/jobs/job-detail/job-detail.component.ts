import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { JobsService } from '../../../core/services/jobs.service';
import { MatchingService } from '../../../core/services/matching.service';
import { ApplicationsService } from '../../../core/services/applications.service';
import { JobOffer } from '../../../core/models/job.models';
import { MatchingResultResponse } from '../../../core/models/matching.models';
import { CommonModule, DatePipe } from '@angular/common';

@Component({
  selector: 'app-job-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, DatePipe],
  templateUrl: './job-detail.component.html',
  styleUrls: ['./job-detail.component.css']
})
export class JobDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private jobsService = inject(JobsService);
  private matchingService = inject(MatchingService);
  private applicationsService = inject(ApplicationsService);

  jobId!: number;
  job: JobOffer | undefined;

  isLoading = false;
  isMatching = false;
  isGenerating = false;

  errorMessage = '';
  successMessage = '';

  matching: MatchingResultResponse | null = null;

  ngOnInit(): void {
    this.jobId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadJob();
  }

  loadJob(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.jobsService.getAll().subscribe({
      next: (jobs) => {
        this.job = (jobs ?? []).find(j => j.id === this.jobId);
        this.isLoading = false;

        if (!this.job) {
          this.errorMessage = 'Offre introuvable.';
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err?.error?.error ?? 'Impossible de charger le détail de l’offre';
      }
    });
  }

  evaluate(): void {
    if (!this.job) {
      return;
    }

    this.isMatching = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.matchingService.evaluate(this.jobId).subscribe({
      next: (result) => {
        this.matching = result;
        this.isMatching = false;
      },
      error: (err) => {
        this.isMatching = false;
        this.errorMessage = err?.error?.error ?? 'Impossible de lancer le matching';
      }
    });
  }

  generateApplication(): void {
    if (!this.job) {
      return;
    }

    this.isGenerating = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.applicationsService.generate(this.jobId).subscribe({
      next: (app) => {
        this.isGenerating = false;
        this.router.navigate(['/applications', app.id]);
      },
      error: (err) => {
        this.isGenerating = false;
        this.errorMessage = err?.error?.error ?? 'Impossible de générer la candidature';
      }
    });
  }

  get scorePercentage(): number {
    if (!this.matching) {
      return 0;
    }

    return Math.round((this.matching.score ?? 0) * 100);
  }
}