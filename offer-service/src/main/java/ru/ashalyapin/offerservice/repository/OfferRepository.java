package ru.ashalyapin.offerservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.ashalyapin.offerservice.entity.Offer;

import java.util.Optional;

public interface OfferRepository extends MongoRepository<Offer, String> {

    Optional<Offer> findByCandidateId(Long candidateId);

    boolean existsByEventId(String eventId);
}