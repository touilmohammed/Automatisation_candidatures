import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { JobOffer } from '../models/job.models';

@Injectable({ providedIn: 'root' })
export class JobsService {
  private readonly apiUrl = `${environment.apiBaseUrl}/jobs`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<JobOffer[]> {
    return this.http.get<JobOffer[]>(this.apiUrl);
  }
}