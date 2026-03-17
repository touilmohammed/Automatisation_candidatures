import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuditLogResponse } from '../models/audit.models';

@Injectable({ providedIn: 'root' })
export class AuditService {
  private readonly apiUrl = `${environment.apiBaseUrl}/audit`;

  constructor(private http: HttpClient) {}

  getMyLogs(): Observable<AuditLogResponse[]> {
    return this.http.get<AuditLogResponse[]>(`${this.apiUrl}/me`);
  }
}