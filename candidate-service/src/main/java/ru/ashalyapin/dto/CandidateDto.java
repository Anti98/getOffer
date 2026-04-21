package ru.ashalyapin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ashalyapin.enums.Grade;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CandidateDto {
    @NotNull
    private String name;
    @NotNull
    private String email;
    private Grade grade;
    private Short experienceYears;
    private Integer salary;
}
