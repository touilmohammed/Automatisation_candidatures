package com.freelanceautomation.backend.notification.controller;

import com.freelanceautomation.backend.notification.dto.NotificationActionResponse;
import com.freelanceautomation.backend.notification.dto.NotificationResponse;
import com.freelanceautomation.backend.notification.service.NotificationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/me")
    public List<NotificationResponse> getMyNotifications(Authentication authentication) {
        return notificationService.getMyNotifications(authentication.getName());
    }

    @PostMapping("/{notificationId}/read")
    public NotificationActionResponse markAsRead(@PathVariable Long notificationId) {
        return notificationService.markAsRead(notificationId);
    }
}