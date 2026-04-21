package ru.ashalyapin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ashalyapin.enums.Grade;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CandidateUpdateDto {
    private String name;
    private String email;
    private Grade grade;
    private Short experienceYears;
    private Integer salary;
}