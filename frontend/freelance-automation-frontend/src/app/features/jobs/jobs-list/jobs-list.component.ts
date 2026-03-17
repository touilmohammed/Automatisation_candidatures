import { CommonModule, DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { JobsService } from '../../../core/services/jobs.service';
import { JobOffer } from '../../../core/models/job.models';

@Component({
  selector: 'app-jobs-list',
  standalone: true,
  imports: [CommonModule, RouterLink, DatePipe],
  templateUrl: './jobs-list.component.html',
  styleUrls: ['./jobs-list.component.css']
})
export class JobsListComponent implements OnInit {
  private jobsService = inject(JobsService);
  private router = inject(Router);

  isLoading = false;
  errorMessage = '';
  jobs: JobOffer[] = [];

  ngOnInit(): void {
    this.loadJobs();
  }

  loadJobs(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.jobsService.getAll().subscribe({
      next: (jobs) => {
        this.jobs = jobs ?? [];
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err?.error?.error ?? 'Impossible de charger les offres';
      }
    });
  }

  openJob(jobId: number): void {
    this.router.navigate(['/jobs', jobId]);
  }

  trackByJobId(_: number, job: JobOffer): number {
    return job.id;
  }
}