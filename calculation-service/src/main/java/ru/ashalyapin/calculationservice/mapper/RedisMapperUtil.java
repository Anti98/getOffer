package ru.ashalyapin.calculationservice.mapper;

import ru.ashalyapin.calculationservice.dto.CoefficientsDto;

import java.util.Map;

import static ru.ashalyapin.calculationservice.constants.RedisKeys.BONUS_PERCENT;
import static ru.ashalyapin.calculationservice.constants.RedisKeys.MARKET;
import static ru.ashalyapin.calculationservice.constants.RedisKeys.SALARY_JUNIOR;
import static ru.ashalyapin.calculationservice.constants.RedisKeys.SALARY_MIDDLE;
import static ru.ashalyapin.calculationservice.constants.RedisKeys.SALARY_SENIOR;

public class RedisMapperUtil {


    public static Map<String, String> toRedisMap(CoefficientsDto dto) {
        return Map.of(
                MARKET, String.valueOf(dto.getMarket()),
                BONUS_PERCENT, String.valueOf(dto.getExperienceBonusPercent()),
                SALARY_JUNIOR, String.valueOf(dto.getBaseSalaryJunior()),
                SALARY_MIDDLE, String.valueOf(dto.getBaseSalaryMiddle()),
                SALARY_SENIOR, String.valueOf(dto.getBaseSalarySenior())
        );
    }

    public static CoefficientsDto mapToDto(Map<Object, Object> map) {
        return CoefficientsDto.builder()
                .market(parseInt(map.get(MARKET)))
                .experienceBonusPercent(parseInt(map.get(BONUS_PERCENT)))
                .baseSalaryJunior(parseInt(map.get(SALARY_JUNIOR)))
                .baseSalaryMiddle(parseInt(map.get(SALARY_MIDDLE)))
                .baseSalarySenior(parseInt(map.get(SALARY_SENIOR)))
                .build();
    }

    private static Integer parseInt(Object value) {
        return Integer.parseInt(value.toString());
    }

}
