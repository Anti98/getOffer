package ru.ashalyapin.calculationservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "coefficients")
public class CoefficientProperties {
    private Integer market;
    private Integer experienceBonusPercent;
    private BaseSalary baseSalary = new BaseSalary();

    @Data
    public static class BaseSalary {
        private Integer junior;
        private Integer middle;
        private Integer senior;
    }
}