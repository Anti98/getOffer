package ru.ashalyapin.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ashalyapin.event.CandidateCreatedEvent;
import ru.ashalyapin.exception.KafkaPublishException;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Log4j2
public class CandidateEventProducer {
    private final KafkaTemplate<String, CandidateCreatedEvent> kafkaTemplate;
    @Value("${app.kafka.topics.candidate-created}")
    private String topic;


    public void send(CandidateCreatedEvent event) {
        String key = event.getCandidateId().toString();

        try {
            var result = kafkaTemplate.send(topic, key, event).get();
            log.info(
                    "Event sent: topic={}, partition={}, offset={}, key={}",
                    topic,
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset(),
                    key
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaPublishException("Interrupted while sending CandidateCreatedEvent", e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            throw new KafkaPublishException("Failed to send CandidateCreatedEvent", cause);
        }
    }
}
