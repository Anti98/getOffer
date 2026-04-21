package ru.ashalyapin.calculationservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.ashalyapin.calculationservice.event.CandidateCreatedEvent;
import ru.ashalyapin.calculationservice.service.CalculationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CandidateEventConsumer {

    private final CalculationService calculationService;

    @KafkaListener(topics = "${app.kafka.topics.candidate-created}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consume(CandidateCreatedEvent event) {
        log.info("Received CandidateCreatedEvent: candidateId={}", event.getCandidateId());
        calculationService.process(event);
    }
}
