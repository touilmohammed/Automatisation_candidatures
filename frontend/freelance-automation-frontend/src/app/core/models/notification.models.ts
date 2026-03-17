export interface NotificationResponse {
  id: number;
  message: string;
  isRead: boolean;
  createdAt: string;
  readAt: string | null;
}

export interface NotificationActionResponse {
  notificationId: number;
  status: string;
}