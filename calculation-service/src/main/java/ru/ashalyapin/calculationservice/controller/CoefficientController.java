package ru.ashalyapin.calculationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ashalyapin.calculationservice.dto.CoefficientsDto;
import ru.ashalyapin.calculationservice.service.CoefficientService;


@RestController
@RequestMapping("/api/coefficients")
@RequiredArgsConstructor
@Tag(name = "Коэффициенты расчёта", description = "Управление коэффициентами для расчёта оффера")
public class CoefficientController {

    private final CoefficientService coefficientService;

    @GetMapping
    @Operation(summary = "Получить все коэффициенты", description = "Возвращает текущие значения всех коэффициентов (рыночный, бонус за опыт, базовые зарплаты).")
    public CoefficientsDto getCoefficients() {
        return coefficientService.getCoefficients();
    }

    @PutMapping
    @Operation(summary = "Обновить все коэффициенты", description = "Полностью заменяет все коэффициенты новыми значениями. Требуется передать полный объект.")
    public void updateCoefficients(@RequestBody CoefficientsDto dto) {
        coefficientService.updateCoefficients(dto);
    }
}