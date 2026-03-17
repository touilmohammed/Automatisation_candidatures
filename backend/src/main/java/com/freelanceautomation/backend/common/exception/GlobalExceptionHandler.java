package com.freelanceautomation.backend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        String message = ex.getMessage();

        if ("EMAIL_ALREADY_USED".equals(message)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "EMAIL_ALREADY_USED"));
        }

        if ("INVALID_CREDENTIALS".equals(message)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "INVALID_CREDENTIALS"));
        }

        if ("PROFILE_NOT_FOUND".equals(message)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "PROFILE_NOT_FOUND"));
        }

        if ("USER_NOT_FOUND".equals(message)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "USER_NOT_FOUND"));
        }

        if ("CV_NOT_FOUND".equals(message)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "CV_NOT_FOUND"));
        }

        if ("UNSUPPORTED_CV_FORMAT".equals(message)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "UNSUPPORTED_CV_FORMAT"));
        }

        if ("EMPTY_FILE".equals(message)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "EMPTY_FILE"));
        }

        if ("INVALID_FILENAME".equals(message)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "INVALID_FILENAME"));
        }

        if ("PDF_TEXT_EXTRACTION_FAILED".equals(message)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "PDF_TEXT_EXTRACTION_FAILED"));
        }

        if ("JOB_NOT_FOUND".equals(message)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "JOB_NOT_FOUND"));
        }

        if ("APPLICATION_NOT_FOUND".equals(message)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "APPLICATION_NOT_FOUND"));
        }

        if ("APPLICATION_NOT_VALIDATED".equals(message)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "APPLICATION_NOT_VALIDATED"));
        }

        if ("NOTIFICATION_NOT_FOUND".equals(message)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "NOTIFICATION_NOT_FOUND"));
        }

         return ResponseEntity.badRequest()
                .body(Map.of("error", message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("VALIDATION_ERROR");

        return ResponseEntity.badRequest()
                .body(Map.of("error", errorMessage));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(Map.of("error", ex.getMessage()));
    }
}