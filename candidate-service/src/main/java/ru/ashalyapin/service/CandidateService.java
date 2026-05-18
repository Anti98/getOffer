package ru.ashalyapin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ashalyapin.dto.CandidateDto;
import ru.ashalyapin.dto.CandidateUpdateDto;
import ru.ashalyapin.entity.CandidateEntity;
import ru.ashalyapin.event.CandidateCreatedEvent;
import ru.ashalyapin.exception.DuplicateCandidateException;
import ru.ashalyapin.exception.NotFoundCandidateException;
import ru.ashalyapin.exception.NothingChangedException;
import ru.ashalyapin.kafka.CandidateEventProducer;
import ru.ashalyapin.mapper.CandidateMapper;
import ru.ashalyapin.repository.CandidateRepository;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateMapper candidateMapper;
    private final CandidateRepository candidateRepository;
    private final CandidateEventProducer eventProducer;


    @Transactional
    public CandidateDto registerCandidate(CandidateDto candidateDto) {

        candidateRepository.findByEmail(candidateDto.getEmail())
                .ifPresent(c -> {
                    throw new DuplicateCandidateException("Candidate with email already exists");
                });

        CandidateEntity entity = candidateMapper.toEntity(candidateDto);
        CandidateEntity saved = candidateRepository.save(entity);

        sendEvent(saved);

        return candidateMapper.toDto(saved);
    }



    public CandidateDto updateCandidate(Long id, CandidateUpdateDto updateDto) {

        CandidateEntity candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new NotFoundCandidateException(
                        String.format("Candidate with id %d not found", id)));

        if (!hasChanges(candidate, updateDto)) {
            throw new NothingChangedException(id);
        }

        if (updateDto.getEmail() != null &&
                !Objects.equals(candidate.getEmail(), updateDto.getEmail())) {

            candidateRepository.findByEmail(updateDto.getEmail())
                    .ifPresent(c -> {
                        throw new DuplicateCandidateException("Candidate with email already exists");
                    });
        }

        candidateMapper.updateEntity(candidate, updateDto);

        CandidateEntity updated = candidateRepository.save(candidate);
        return candidateMapper.toDto(updated);
    }


    public void recalculate(Long id) {

        CandidateEntity candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new NotFoundCandidateException(
                        String.format("Candidate with id %d not found", id)));

        sendEvent(candidate);
    }


    private boolean hasChanges(CandidateEntity entity, CandidateUpdateDto dto) {
        return (dto.getName() != null && !Objects.equals(entity.getName(), dto.getName()))
                || (dto.getEmail() != null && !Objects.equals(entity.getEmail(), dto.getEmail()))
                || (dto.getGrade() != null && entity.getGrade() != dto.getGrade())
                || (dto.getExperienceYears() != null &&
                !Objects.equals(entity.getExperienceYears(), dto.getExperienceYears()))
                || (dto.getSalary() != null &&
                !Objects.equals(entity.getSalary(), dto.getSalary()));
    }

    private void sendEvent(CandidateEntity candidate) {
        CandidateCreatedEvent event = candidateMapper.toEvent(candidate);
        event.setEventId(UUID.randomUUID().toString());
        eventProducer.send(event);
    }
}

