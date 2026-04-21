package ru.ashalyapin.calculationservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaErrorHandlerConfig {

    @Bean
    public DefaultErrorHandler errorHandler() {
        return new DefaultErrorHandler(
                (record, exception) -> {
                    log.error("Failed message: topic={}, value={}",
                            record.topic(),
                            record.value(),
                            exception);
                },
                new FixedBackOff(0L, 0)
        );
    }
}