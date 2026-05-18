package ru.ashalyapin.calculationservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ashalyapin.calculationservice.event.CalculationCompletedEvent;
import ru.ashalyapin.calculationservice.exception.InfrastructureException;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculationEventProducer {

    private final KafkaTemplate<String, CalculationCompletedEvent> kafkaTemplate;

    @Value("${app.kafka.topics.calculation-completed}")
    private String topic;

    public void send(CalculationCompletedEvent event) {
        String key = event.getCandidateId().toString();

        try {
            var result = kafkaTemplate.send(topic, key, event).get();
            log.info(
                    "CalculationCompletedEvent sent: candidateId={}, partition={}, offset={}",
                    event.getCandidateId(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset()
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InfrastructureException("Interrupted while sending CalculationCompletedEvent", e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            throw new InfrastructureException("Failed to send CalculationCompletedEvent", cause);
        }
    }
}
