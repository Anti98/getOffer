package ru.ashalyapin.calculationservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateCreatedEvent {
    private String eventId;
    private Long candidateId;
    private String name;
    private String email;
    private String grade; // "JUNIOR", "MIDDLE", "SENIOR"
    private Integer experienceYears;
    private Integer salary;
}