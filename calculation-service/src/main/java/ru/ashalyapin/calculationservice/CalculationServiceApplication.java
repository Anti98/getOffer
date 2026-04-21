package ru.ashalyapin.calculationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class CalculationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalculationServiceApplication.class, args);
    }

}
