package ru.ashalyapin.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateCandidateException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateCandidateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        "CANDIDATE_ALREADY_EXISTS",
                        Instant.now()
                ));
    }
    @ExceptionHandler(NotFoundCandidateException.class)
    public ResponseEntity<ErrorResponse> handleNotfound(NotFoundCandidateException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        "CANDIDATE_NOT_FOUND",
                        Instant.now()
                ));
    }
    @ExceptionHandler(NothingChangedException.class)
    public ResponseEntity<ErrorResponse> handleNothingChanged(NothingChangedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        "NOTHING_CHANGED",
                        Instant.now()
                ));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    // ⚫ 4. Любая другая ошибка
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "Internal server error",
                        "INTERNAL_ERROR",
                        Instant.now()
                ));
    }
}