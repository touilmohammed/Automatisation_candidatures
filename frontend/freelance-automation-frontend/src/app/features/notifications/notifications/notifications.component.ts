import { CommonModule, DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { NotificationsService } from '../../../core/services/notifications.service';
import { NotificationResponse } from '../../../core/models/notification.models';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit {
  private notificationsService = inject(NotificationsService);

  isLoading = false;
  errorMessage = '';
  successMessage = '';

  notifications: NotificationResponse[] = [];
  processingIds = new Set<number>();

  ngOnInit(): void {
    this.loadNotifications();
  }

  loadNotifications(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.notificationsService.getMyNotifications().subscribe({
      next: (notifications) => {
        this.notifications = (notifications ?? []).sort((a, b) =>
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err?.error?.error ?? 'Impossible de charger les notifications';
      }
    });
  }

  markAsRead(notificationId: number): void {
    this.processingIds.add(notificationId);
    this.errorMessage = '';
    this.successMessage = '';

    this.notificationsService.markAsRead(notificationId).subscribe({
      next: () => {
        this.processingIds.delete(notificationId);
        this.successMessage = 'Notification marquée comme lue.';
        this.loadNotifications();
      },
      error: (err) => {
        this.processingIds.delete(notificationId);
        this.errorMessage = err?.error?.error ?? 'Impossible de marquer la notification comme lue';
      }
    });
  }

  isProcessing(notificationId: number): boolean {
    return this.processingIds.has(notificationId);
  }

  get unreadCount(): number {
    return this.notifications.filter(n => !n.isRead).length;
  }
}