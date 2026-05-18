package ru.ashalyapin.offerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.ashalyapin.offerservice.entity.Offer;
import ru.ashalyapin.offerservice.event.CalculationCompletedEvent;
import ru.ashalyapin.offerservice.exception.OfferNotFoundException;
import ru.ashalyapin.offerservice.mapper.OfferMapper;
import ru.ashalyapin.offerservice.model.OfferHistory;
import ru.ashalyapin.offerservice.model.OfferHistoryResponse;
import ru.ashalyapin.offerservice.model.OfferResponse;
import ru.ashalyapin.offerservice.model.OfferStatus;
import ru.ashalyapin.offerservice.repository.OfferRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;


    public void handleCalculation(CalculationCompletedEvent event) {
        Offer offer = offerRepository
                .findByCandidateId(event.getCandidateId())
                .orElseGet(Offer::new);

        boolean isNew = offer.getId() == null;

        if (offer.getHistory() == null) {
            offer.setHistory(new ArrayList<>());
        }

        boolean businessChanged = isNew
                || !Objects.equals(offer.getRecommendedSalary(), event.getRecommendedSalary())
                || !Objects.equals(offer.getGrade(), event.getGrade())
                || !Objects.equals(offer.getExperienceYears(), event.getExperienceYears());

        boolean dataChanged =
                !Objects.equals(offer.getCurrentSalary(), event.getCurrentSalary());

        if (!businessChanged && !dataChanged) {
            return;
        }

        offer.setCandidateId(event.getCandidateId());
        offer.setEventId(event.getEventId());

        offer.setGrade(event.getGrade());
        offer.setExperienceYears(event.getExperienceYears());

        offer.setCurrentSalary(event.getCurrentSalary());
        offer.setRecommendedSalary(event.getRecommendedSalary());

        if (isNew) {
            offer.setFinalSalary(event.getRecommendedSalary());
            offer.setCreatedAt(Instant.now());
            offer.setStatus(OfferStatus.CREATED);
        } else if (businessChanged) {
            offer.setStatus(OfferStatus.UPDATED);
        }


        if (businessChanged) {
            OfferHistory history = new OfferHistory(
                    Instant.now(),
                    event.getRecommendedSalary(),
                    offer.getFinalSalary(),
                    isNew ? "CREATED" : "UPDATED"
            );

            offer.getHistory().add(history);
        }

        try {
            offerRepository.save(offer);
        } catch (DuplicateKeyException ex) {
            log.info("Duplicate calculation event skipped: eventId={}", event.getEventId());
        }
    }


    public OfferResponse getOffer(Long candidateId) {

        Offer offer = offerRepository.findByCandidateId(candidateId)
                .orElseThrow(() -> new OfferNotFoundException(candidateId));

        return offerMapper.toResponse(offer);
    }

    public List<OfferHistoryResponse> getHistory(Long candidateId) {

        Offer offer = offerRepository.findByCandidateId(candidateId)
                .orElseThrow(() -> new OfferNotFoundException(candidateId));

        if (offer.getHistory() == null) {
            return List.of();
        }

        return offerMapper.toHistoryList(offer.getHistory());
    }

    public List<OfferResponse> getAllOffers() {

        return offerRepository.findAll().stream()
                .map(offerMapper::toResponse)
                .toList();
    }
}
