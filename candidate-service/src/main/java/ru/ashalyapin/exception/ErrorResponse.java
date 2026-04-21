package ru.ashalyapin.exception;

import java.time.Instant;

public record ErrorResponse(
        String message,
        String code,
        Instant timestamp
) {}