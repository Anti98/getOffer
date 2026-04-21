package ru.ashalyapin.calculationservice.exception;

public class InfrastructureException extends ServiceException {
    public InfrastructureException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}