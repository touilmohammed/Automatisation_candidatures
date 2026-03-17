import { CommonModule, DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { AuditService } from '../../../core/services/audit.service';
import { AuditLogResponse } from '../../../core/models/audit.models';

@Component({
  selector: 'app-audit',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './audit.component.html',
  styleUrls: ['./audit.component.css']
})
export class AuditComponent implements OnInit {
  private auditService = inject(AuditService);

  isLoading = false;
  errorMessage = '';
  logs: AuditLogResponse[] = [];

  ngOnInit(): void {
    this.loadLogs();
  }

  loadLogs(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.auditService.getMyLogs().subscribe({
      next: (logs) => {
        this.logs = (logs ?? []).sort((a, b) =>
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err?.error?.error ?? 'Impossible de charger l’historique';
      }
    });
  }

  trackByLogId(_: number, log: AuditLogResponse): number {
    return log.id;
  }
}