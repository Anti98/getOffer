package ru.ashalyapin.offerservice.model;

import lombok.Data;

import java.time.Instant;

@Data
public class OfferResponse {

    private Long candidateId;
    private String status;

    private Integer recommendedSalary;
    private Integer finalSalary;

    private String grade;
    private Integer experienceYears;

    private Integer currentSalary;
    private Instant createdAt;
}