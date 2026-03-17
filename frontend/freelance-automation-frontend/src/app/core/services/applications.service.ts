import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  ApplicationActionResponse,
  ApplicationResponse,
  ApplicationSubmitResponse
} from '../models/application.models';

@Injectable({ providedIn: 'root' })
export class ApplicationsService {
  private readonly apiUrl = `${environment.apiBaseUrl}/applications`;

  constructor(private http: HttpClient) {}

  generate(jobId: number): Observable<ApplicationResponse> {
    return this.http.post<ApplicationResponse>(`${this.apiUrl}/jobs/${jobId}/generate`, {});
  }

  getById(applicationId: number): Observable<ApplicationResponse> {
    return this.http.get<ApplicationResponse>(`${this.apiUrl}/${applicationId}`);
  }

  validate(applicationId: number): Observable<ApplicationActionResponse> {
    return this.http.post<ApplicationActionResponse>(`${this.apiUrl}/${applicationId}/validate`, {});
  }

  reject(applicationId: number): Observable<ApplicationActionResponse> {
    return this.http.post<ApplicationActionResponse>(`${this.apiUrl}/${applicationId}/reject`, {});
  }

  submit(applicationId: number): Observable<ApplicationSubmitResponse> {
    return this.http.post<ApplicationSubmitResponse>(`${this.apiUrl}/${applicationId}/submit`, {});
  }
}