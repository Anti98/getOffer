package ru.ashalyapin.calculationservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CalculationCompletedEvent {

    private String eventId;
    private Long candidateId;

    private String grade;
    private Integer experienceYears;

    private Integer currentSalary;
    private Integer recommendedSalary;
}