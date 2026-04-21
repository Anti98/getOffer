package ru.ashalyapin.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ashalyapin.event.CandidateCreatedEvent;

@Service
@RequiredArgsConstructor
@Log4j2
public class CandidateEventProducer {
    private final KafkaTemplate<String, CandidateCreatedEvent> kafkaTemplate;
    @Value("${app.kafka.topics.candidate-created}")
    private String topic;


    public void send(CandidateCreatedEvent event) {
        String key = event.getCandidateId().toString();
        kafkaTemplate.send(topic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Event sent: topic={}, offset={}, key={}", topic, result.getRecordMetadata().offset(), key);
                    } else {
                        log.error("Failed to send event", ex);
                    }
                });
    }
}
