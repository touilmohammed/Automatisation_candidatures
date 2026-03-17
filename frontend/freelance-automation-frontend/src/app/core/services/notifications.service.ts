import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { NotificationActionResponse, NotificationResponse } from '../models/notification.models';

@Injectable({ providedIn: 'root' })
export class NotificationsService {
  private readonly apiUrl = `${environment.apiBaseUrl}/notifications`;

  constructor(private http: HttpClient) {}

  getMyNotifications(): Observable<NotificationResponse[]> {
    return this.http.get<NotificationResponse[]>(`${this.apiUrl}/me`);
  }

  markAsRead(notificationId: number): Observable<NotificationActionResponse> {
    return this.http.post<NotificationActionResponse>(`${this.apiUrl}/${notificationId}/read`, {});
  }
}