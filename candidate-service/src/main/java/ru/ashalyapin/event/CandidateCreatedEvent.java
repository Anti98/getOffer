package ru.ashalyapin.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CandidateCreatedEvent {
    private String eventId;
    private Long candidateId;
    private String name;
    private String email;
    private String grade;
    private Integer experienceYears;
    private Integer salary;
}
