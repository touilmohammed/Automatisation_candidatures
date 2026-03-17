package com.freelanceautomation.backend.notification.service;

import com.freelanceautomation.backend.notification.dto.NotificationActionResponse;
import com.freelanceautomation.backend.notification.dto.NotificationResponse;
import com.freelanceautomation.backend.notification.entity.Notification;
import com.freelanceautomation.backend.notification.repository.NotificationRepository;
import com.freelanceautomation.backend.user.entity.User;
import com.freelanceautomation.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public void createNotification(String email, String message) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        Notification notification = Notification.builder()
                .user(user)
                .message(message)
                .isRead(false)
                .createdAt(Instant.now())
                .build();

        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getMyNotifications(String email) {
        return notificationRepository.findByUserEmailOrderByCreatedAtDesc(email)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public NotificationActionResponse markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("NOTIFICATION_NOT_FOUND"));

        notification.setIsRead(true);
        notification.setReadAt(Instant.now());

        Notification saved = notificationRepository.save(notification);

        return new NotificationActionResponse(saved.getId(), "READ");
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.getIsRead(),
                notification.getCreatedAt(),
                notification.getReadAt()
        );
    }
}