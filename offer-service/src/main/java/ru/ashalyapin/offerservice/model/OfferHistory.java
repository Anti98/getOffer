package ru.ashalyapin.offerservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferHistory {

    private Instant timestamp;

    private Integer recommendedSalary;
    private Integer finalSalary;

    private String action;
}