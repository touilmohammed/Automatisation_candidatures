import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AiGenerationResponse } from '../models/ai.models';

@Injectable({ providedIn: 'root' })
export class AiService {
  private readonly apiUrl = `${environment.apiBaseUrl}/ai`;

  constructor(private http: HttpClient) {}

  generate(jobId: number): Observable<AiGenerationResponse> {
    return this.http.post<AiGenerationResponse>(`${this.apiUrl}/jobs/${jobId}/generate`, {});
  }
}