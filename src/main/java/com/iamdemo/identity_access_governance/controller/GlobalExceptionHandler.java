package com.iamdemo.identity_access_governance.controller;

import com.iamdemo.identity_access_governance.service.DuplicateResourceException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String,Object>> handleDuplicate(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            Map.of("timestamp", Instant.now().toString(),
                   "status", 409,
                   "error", "Conflict",
                   "message", ex.getMessage()));
    }
}
