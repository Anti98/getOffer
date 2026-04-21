package ru.ashalyapin.calculationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ashalyapin.calculationservice.config.CoefficientProperties;
import ru.ashalyapin.calculationservice.dto.CoefficientsDto;
import ru.ashalyapin.calculationservice.repository.RedisCoefficientRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoefficientService {

    private final RedisCoefficientRepository repository;
    private final CoefficientProperties properties;


    public CoefficientsDto getCoefficients() {
        return repository.find()
                .orElseGet(this::loadFromPropertiesAndCache);
    }

    public void updateCoefficients(CoefficientsDto dto) {
        repository.save(dto);
        log.info("Coefficients updated: {}", dto);
    }


    private CoefficientsDto loadFromPropertiesAndCache() {
        log.warn("Cache miss → loading coefficients from properties");
        CoefficientsDto dto = propertiesToDto();
        repository.save(dto);
        return dto;
    }

    private CoefficientsDto propertiesToDto() {
        return CoefficientsDto.builder()
                .market(properties.getMarket())
                .experienceBonusPercent(properties.getExperienceBonusPercent())
                .baseSalaryJunior(properties.getBaseSalary().getJunior())
                .baseSalaryMiddle(properties.getBaseSalary().getMiddle())
                .baseSalarySenior(properties.getBaseSalary().getSenior())
                .build();
    }
}