import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import ru.ashalyapin.service.CandidateService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {

    @Mock
    private CandidateRepository repository;

    @Mock
    private CandidateMapper mapper;

    @Mock
    private CandidateEventProducer producer;

    @InjectMocks
    private CandidateService service;



    @Test
    void shouldCreateCandidateAndSendEvent() {
        CandidateDto dto = new CandidateDto();
        dto.setEmail("test@mail.com");

        CandidateEntity entity = new CandidateEntity();
        CandidateEntity saved = new CandidateEntity();
        saved.setId(1L);

        CandidateCreatedEvent event = new CandidateCreatedEvent();
        CandidateDto responseDto = new CandidateDto();

        when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toEvent(saved)).thenReturn(event);
        when(mapper.toDto(saved)).thenReturn(responseDto);

        CandidateDto result = service.registerCandidate(dto);

        assertNotNull(result);

        verify(repository).findByEmail(dto.getEmail());
        verify(repository).save(entity);
        verify(producer).send(event);
        verify(mapper).toDto(saved);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        CandidateDto dto = new CandidateDto();
        dto.setEmail("test@mail.com");

        when(repository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.of(new CandidateEntity()));

        assertThrows(DuplicateCandidateException.class, () ->
                service.registerCandidate(dto)
        );

        verify(repository, never()).save(any());
        verify(producer, never()).send(any());
    }


    @Test
    void shouldThrowNotFound_whenUpdatingNonExistingCandidate() {
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundCandidateException.class, () ->
                service.updateCandidate(id, new CandidateUpdateDto())
        );

        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowNothingChanged_whenNoFieldsUpdated() {
        Long id = 1L;

        CandidateEntity entity = new CandidateEntity();
        entity.setId(id);
        entity.setName("John");
        entity.setEmail("john@mail.com");

        CandidateUpdateDto updateDto = new CandidateUpdateDto();
        // все поля null

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        assertThrows(NothingChangedException.class, () ->
                service.updateCandidate(id, updateDto)
        );

        verify(repository, never()).save(any());
        verify(mapper, never()).updateEntity(any(), any());
    }

    @Test
    void shouldUpdateCandidateSuccessfully() {
        Long id = 1L;

        CandidateEntity entity = new CandidateEntity();
        entity.setId(id);
        entity.setName("John");

        CandidateUpdateDto updateDto = new CandidateUpdateDto();
        updateDto.setName("Mike");

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        service.updateCandidate(id, updateDto);

        verify(mapper).updateEntity(entity, updateDto);
        verify(repository).save(entity);
    }

    @Test
    void shouldThrowDuplicate_whenUpdatingEmailToExisting() {
        Long id = 1L;

        CandidateEntity entity = new CandidateEntity();
        entity.setId(id);
        entity.setEmail("old@mail.com");

        CandidateUpdateDto updateDto = new CandidateUpdateDto();
        updateDto.setEmail("new@mail.com");

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.findByEmail("new@mail.com"))
                .thenReturn(Optional.of(new CandidateEntity()));

        assertThrows(DuplicateCandidateException.class, () ->
                service.updateCandidate(id, updateDto)
        );

        verify(repository, never()).save(any());
    }


    @Test
    void shouldThrowNotFound_whenRecalculateNonExistingCandidate() {
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundCandidateException.class, () ->
                service.recalculate(id)
        );

        verify(producer, never()).send(any());
    }

    @Test
    void shouldSendEvent_whenRecalculateCalled() {
        Long id = 1L;

        CandidateEntity entity = new CandidateEntity();
        CandidateCreatedEvent event = new CandidateCreatedEvent();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toEvent(entity)).thenReturn(event);

        service.recalculate(id);

        verify(producer).send(event);
    }
}