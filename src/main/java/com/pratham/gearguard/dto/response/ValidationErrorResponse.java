package com.pratham.gearguard.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        String message,
        Map<String, String> fieldErrors,
        int status,
        LocalDateTime timestamp
) {
  public ValidationErrorResponse(Map<String, String> fieldErrors) {
    this("Validation failed", fieldErrors, 400, LocalDateTime.now());
  }
}
