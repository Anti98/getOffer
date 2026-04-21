package ru.ashalyapin.offerservice.exception;

import lombok.Getter;

@Getter
public class OfferNotFoundException extends RuntimeException {

    private final Long candidateId;

    public OfferNotFoundException(Long candidateId) {
        super("Offer not found for candidateId: " + candidateId);
        this.candidateId = candidateId;
    }

}