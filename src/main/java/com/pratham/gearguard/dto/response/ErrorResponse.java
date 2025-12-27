package com.pratham.gearguard.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String error,
        int status,
        LocalDateTime timestamp
) {
  public ErrorResponse(String message) {
    this(message, null, 400, LocalDateTime.now());
  }

  public ErrorResponse(String message, String error, int status) {
    this(message, error, status, LocalDateTime.now());
  }
}
