package ru.ashalyapin.offerservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.ashalyapin.offerservice.event.CalculationCompletedEvent;
import ru.ashalyapin.offerservice.service.OfferService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalculationCompletedConsumer {

    private final OfferService offerService;

    @KafkaListener(topics = "${app.kafka.topics.calculation-completed}", groupId = "${spring.kafka.consumer.group-id}")
    public void handle(CalculationCompletedEvent event) {

        log.info("Received event: {}", event.getEventId());

        offerService.handleCalculation(event);
    }
}
