import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ProfileResponse, UpsertProfileRequest } from '../models/profile.models';

@Injectable({ providedIn: 'root' })
export class ProfileService {
  private readonly apiUrl = `${environment.apiBaseUrl}/profile`;

  constructor(private http: HttpClient) {}

  getMyProfile(): Observable<ProfileResponse> {
    return this.http.get<ProfileResponse>(`${this.apiUrl}/me`);
  }

  upsertMyProfile(payload: UpsertProfileRequest): Observable<ProfileResponse> {
    return this.http.put<ProfileResponse>(`${this.apiUrl}/me`, payload);
  }
}