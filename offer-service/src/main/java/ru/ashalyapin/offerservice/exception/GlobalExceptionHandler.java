package ru.ashalyapin.offerservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OfferNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleOfferNotFound(OfferNotFoundException ex) {
        return new ErrorResponse(
                "OFFER_NOT_FOUND",
                ex.getMessage(),
                Instant.now()
        );
    }
}