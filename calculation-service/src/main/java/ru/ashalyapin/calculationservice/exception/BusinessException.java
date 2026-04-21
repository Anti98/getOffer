package ru.ashalyapin.calculationservice.exception;

public class BusinessException extends ServiceException {
    public BusinessException(String message) {
        super(message);
    }
}