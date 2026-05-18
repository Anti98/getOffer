package ru.ashalyapin.offerservice.exception;

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
        DefaultErrorHandler handler = new DefaultErrorHandler(
                (record, exception) -> {
                    log.error(
                            "Retries exhausted for message: topic={}, partition={}, offset={}, value={}",
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.value(),
                            exception
                    );
                    throw new IllegalStateException(
                            "Retries exhausted for Kafka record at offset " + record.offset(),
                            exception
                    );
                },
                new FixedBackOff(1000L, 3)
        );
        handler.setAckAfterHandle(false);
        return handler;
    }
}
