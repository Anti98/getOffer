package ru.ashalyapin.offerservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ashalyapin.offerservice.entity.Offer;
import ru.ashalyapin.offerservice.event.CalculationCompletedEvent;
import ru.ashalyapin.offerservice.exception.OfferNotFoundException;
import ru.ashalyapin.offerservice.mapper.OfferMapper;
import ru.ashalyapin.offerservice.model.OfferResponse;
import ru.ashalyapin.offerservice.model.OfferStatus;
import ru.ashalyapin.offerservice.repository.OfferRepository;
import ru.ashalyapin.offerservice.service.OfferService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private OfferMapper offerMapper;

    @InjectMocks
    private OfferService offerService;

    private CalculationCompletedEvent event;

    @BeforeEach
    void setUp() {
        event = new CalculationCompletedEvent(
                "event-1",
                1L,
                "MIDDLE",
                3,
                1000,
                1500
        );
    }


    @Test
    void shouldCreateNewOffer() {

        when(offerRepository.existsByEventId("event-1")).thenReturn(false);
        when(offerRepository.findByCandidateId(1L)).thenReturn(Optional.empty());

        offerService.handleCalculation(event);

        ArgumentCaptor<Offer> captor = ArgumentCaptor.forClass(Offer.class);
        verify(offerRepository).save(captor.capture());

        Offer saved = captor.getValue();

        assertThat(saved.getCandidateId()).isEqualTo(1L);
        assertThat(saved.getStatus()).isEqualTo(OfferStatus.CREATED);
        assertThat(saved.getRecommendedSalary()).isEqualTo(1500);
        assertThat(saved.getFinalSalary()).isEqualTo(1500);
        assertThat(saved.getHistory()).hasSize(1);
    }

    // =========================
    // 🔁 UPDATE EXISTING OFFER
    // =========================

    @Test
    void shouldUpdateOfferWhenBusinessChanged() {

        Offer existing = Offer.builder()
                .id("id-1")
                .candidateId(1L)
                .recommendedSalary(1000)
                .finalSalary(1100)
                .grade("JUNIOR")
                .experienceYears(1)
                .build();

        when(offerRepository.existsByEventId("event-1")).thenReturn(false);
        when(offerRepository.findByCandidateId(1L)).thenReturn(Optional.of(existing));

        offerService.handleCalculation(event);

        verify(offerRepository).save(existing);

        assertThat(existing.getStatus()).isEqualTo(OfferStatus.UPDATED);
        assertThat(existing.getRecommendedSalary()).isEqualTo(1500);
        assertThat(existing.getHistory()).isNotEmpty();
    }


    @Test
    void shouldSkipWhenEventAlreadyProcessed() {

        when(offerRepository.existsByEventId("event-1")).thenReturn(true);

        offerService.handleCalculation(event);

        verify(offerRepository, never()).save(any());
    }

    @Test
    void shouldSkipWhenNoChanges() {

        Offer existing = Offer.builder()
                .id("id-1")
                .candidateId(1L)
                .recommendedSalary(1500)
                .grade("MIDDLE")
                .experienceYears(3)
                .currentSalary(1000)
                .build();

        when(offerRepository.existsByEventId("event-1")).thenReturn(false);
        when(offerRepository.findByCandidateId(1L)).thenReturn(Optional.of(existing));

        offerService.handleCalculation(event);

        verify(offerRepository, never()).save(any());
    }

    // =========================
    // ✅ GET OFFER SUCCESS
    // =========================

    @Test
    void shouldReturnOffer() {

        Offer offer = new Offer();
        offer.setCandidateId(1L);

        OfferResponse response = new OfferResponse();
        response.setCandidateId(1L);

        when(offerRepository.findByCandidateId(1L)).thenReturn(Optional.of(offer));
        when(offerMapper.toResponse(offer)).thenReturn(response);

        OfferResponse result = offerService.getOffer(1L);

        assertThat(result.getCandidateId()).isEqualTo(1L);
    }


    @Test
    void shouldThrowWhenOfferNotFound() {

        when(offerRepository.findByCandidateId(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> offerService.getOffer(1L))
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessage("Offer not found for candidateId: 1");
    }
}