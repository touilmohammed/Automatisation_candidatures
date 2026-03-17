import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CvResponse, CvTextResponse } from '../models/cv.models';

@Injectable({ providedIn: 'root' })
export class CvService {
  private readonly apiUrl = `${environment.apiBaseUrl}/cv`;

  constructor(private http: HttpClient) {}

  upload(file: File): Observable<CvResponse> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<CvResponse>(`${this.apiUrl}/upload`, formData);
  }

  getMyCv(): Observable<CvResponse> {
    return this.http.get<CvResponse>(`${this.apiUrl}/me`);
  }

  getMyCvText(): Observable<CvTextResponse> {
    return this.http.get<CvTextResponse>(`${this.apiUrl}/me/text`);
  }
}