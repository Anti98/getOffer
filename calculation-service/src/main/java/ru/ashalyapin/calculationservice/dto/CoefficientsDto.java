package ru.ashalyapin.calculationservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class CoefficientsDto {
    @Schema(description = "Рыночный коэффициент (в процентах, где 100 = 1.0)")
    private Integer market;

    @Schema(description = "Процент бонуса за каждый год опыта")
    private Integer experienceBonusPercent;

    @Schema(description = "Базовая зарплата для Junior")
    private Integer baseSalaryJunior;

    @Schema(description = "Базовая зарплата для Middle")
    private Integer baseSalaryMiddle;

    @Schema(description = "Базовая зарплата для Senior")
    private Integer baseSalarySenior;
}