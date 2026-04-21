package ru.ashalyapin.calculationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ashalyapin.calculationservice.dto.CoefficientsDto;
import ru.ashalyapin.calculationservice.event.CalculationCompletedEvent;
import ru.ashalyapin.calculationservice.event.CandidateCreatedEvent;
import ru.ashalyapin.calculationservice.exception.BusinessException;
import ru.ashalyapin.calculationservice.kafka.CalculationEventProducer;
import ru.ashalyapin.calculationservice.repository.RedisCoefficientRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculationService {
    private final RedisCoefficientRepository redisCoefficientRepository;
    private final CalculationEventProducer eventProducer;
    private final CoefficientService coefficientService;


    public void process(CandidateCreatedEvent event) {
        String eventId = event.getEventId();

        if (eventId == null) {
            log.warn("Event without eventId, fallback to candidateId");
            eventId = String.valueOf(event.getCandidateId());
        }

        if (!redisCoefficientRepository.tryMarkProcessed(eventId)) {
            log.info("Duplicate event skipped: {}", eventId);
            return;
        }

        try {
            log.info("Processing calculation for candidate: {}", event.getCandidateId());
            calculate(event);

        } catch (Exception e) {
            redisCoefficientRepository.removeProcessedMark(eventId);
            log.error("Error processing event {}, rollback idempotency mark", eventId, e);
            throw e;
        }
    }

    private void calculate(CandidateCreatedEvent event) {

        CoefficientsDto coefficients = coefficientService.getCoefficients();

        int baseSalary = switch (event.getGrade()) {
            case "JUNIOR" -> coefficients.getBaseSalaryJunior();
            case "MIDDLE" -> coefficients.getBaseSalaryMiddle();
            case "SENIOR" -> coefficients.getBaseSalarySenior();
            default -> throw new BusinessException("Invalid grade: " + event.getGrade());
        };

        double marketFactor = coefficients.getMarket() / 100.0;

        double experienceFactor = 1 +
                (event.getExperienceYears() * coefficients.getExperienceBonusPercent() / 100.0);
        double calculatedSalary = baseSalary * marketFactor * experienceFactor;

        Integer recommendedSalary = (int) Math.round(calculatedSalary);

        log.info("""
                        Calculation result:
                        candidateId={}
                        grade={}
                        baseSalary={}
                        marketFactor={}
                        experienceFactor={}
                        recommendedSalary={}
                        """,
                event.getCandidateId(),
                event.getGrade(),
                baseSalary,
                marketFactor,
                experienceFactor,
                recommendedSalary
        );
        CalculationCompletedEvent resultEvent = CalculationCompletedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .candidateId(event.getCandidateId())
                .grade(event.getGrade())
                .experienceYears(event.getExperienceYears())
                .currentSalary(event.getSalary())
                .recommendedSalary(recommendedSalary)
                .build();


        eventProducer.send(resultEvent);
    }
}