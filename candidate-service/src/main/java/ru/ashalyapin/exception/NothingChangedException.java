package ru.ashalyapin.exception;

public class NothingChangedException extends RuntimeException {
    public NothingChangedException(Long id) {
        super(String.format("Candidate with id %d has no changes", id));
    }
}