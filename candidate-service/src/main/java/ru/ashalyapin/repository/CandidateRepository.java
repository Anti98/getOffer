package ru.ashalyapin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ashalyapin.entity.CandidateEntity;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<CandidateEntity, Long> {
    Optional<CandidateEntity> findByEmail(String email);
}
