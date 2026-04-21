package ru.ashalyapin.offerservice.model;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class OfferHistoryResponse {

    private Instant timestamp;

    private Integer recommendedSalary;
    private Integer finalSalary;

    private String action;
}