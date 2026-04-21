package ru.ashalyapin.calculationservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ashalyapin.calculationservice.dto.CoefficientsDto;
import ru.ashalyapin.calculationservice.event.CandidateCreatedEvent;
import ru.ashalyapin.calculationservice.exception.BusinessException;
import ru.ashalyapin.calculationservice.kafka.CalculationEventProducer;
import ru.ashalyapin.calculationservice.repository.RedisCoefficientRepository;
import ru.ashalyapin.calculationservice.service.CalculationService;
import ru.ashalyapin.calculationservice.service.CoefficientService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CalculationServiceTest {

    private RedisCoefficientRepository repository;
    private CalculationEventProducer producer;
    private CoefficientService coefficientService;

    private CalculationService service;

    @BeforeEach
    void setUp() {
        repository = mock(RedisCoefficientRepository.class);
        producer = mock(CalculationEventProducer.class);
        coefficientService = mock(CoefficientService.class);

        service = new CalculationService(repository, producer, coefficientService);
    }

    @Test
    void shouldCalculateAndSendEvent() {
        // given
        CandidateCreatedEvent event = new CandidateCreatedEvent(
                "event-1",
                1L,
                "Test",
                "test@mail.com",
                "MIDDLE",
                3,
                200000
        );

        CoefficientsDto coefficients = CoefficientsDto.builder()
                .market(100)
                .experienceBonusPercent(10)
                .baseSalaryJunior(100000)
                .baseSalaryMiddle(200000)
                .baseSalarySenior(300000)
                .build();

        when(repository.tryMarkProcessed("event-1")).thenReturn(true);
        when(coefficientService.getCoefficients()).thenReturn(coefficients);

        service.process(event);

        verify(producer, times(1)).send(any());

    }


    @Test
    void shouldThrowExceptionWhenInvalidGrade() {
        // given
        CandidateCreatedEvent event = new CandidateCreatedEvent(
                "event-2",
                1L,
                "Test",
                "test@mail.com",
                "INVALID",
                3,
                200000
        );

        when(repository.tryMarkProcessed("event-2")).thenReturn(true);
        when(coefficientService.getCoefficients()).thenReturn(
                CoefficientsDto.builder()
                        .market(100)
                        .experienceBonusPercent(10)
                        .baseSalaryJunior(100000)
                        .baseSalaryMiddle(200000)
                        .baseSalarySenior(300000)
                        .build()
        );

        assertThrows(BusinessException.class, () -> service.process(event));

        verify(producer, never()).send(any());
    }
}