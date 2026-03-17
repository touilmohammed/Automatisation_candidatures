import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { MatchingResultResponse } from '../models/matching.models';

@Injectable({ providedIn: 'root' })
export class MatchingService {
  private readonly apiUrl = `${environment.apiBaseUrl}/matching`;

  constructor(private http: HttpClient) {}

  evaluate(jobId: number): Observable<MatchingResultResponse> {
    return this.http.post<MatchingResultResponse>(`${this.apiUrl}/jobs/${jobId}/evaluate`, {});
  }
}