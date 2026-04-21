package ru.ashalyapin.exception;

public class NotFoundCandidateException extends RuntimeException {
    public NotFoundCandidateException(String message) {
        super(message);
    }
}