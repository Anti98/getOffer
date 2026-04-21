package ru.ashalyapin.offerservice.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CalculationCompletedEvent {

    private String eventId;
    private Long candidateId;

    private String grade;
    private Integer experienceYears;

    private Integer currentSalary;
    private Integer recommendedSalary;
}