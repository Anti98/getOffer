package ru.ashalyapin.calculationservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ashalyapin.calculationservice.event.CalculationCompletedEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculationEventProducer {

    private final KafkaTemplate<String, CalculationCompletedEvent> kafkaTemplate;

    @Value("${app.kafka.topics.calculation-completed}")
    private String topic;

    public void send(CalculationCompletedEvent event) {
        String key = event.getCandidateId().toString();

        kafkaTemplate.send(topic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("CalculationCompletedEvent sent: candidateId={}, offset={}",
                                event.getCandidateId(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send CalculationCompletedEvent", ex);
                    }
                });
    }
}
