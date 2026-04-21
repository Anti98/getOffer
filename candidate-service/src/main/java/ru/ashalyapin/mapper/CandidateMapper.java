package ru.ashalyapin.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.ashalyapin.dto.CandidateDto;
import ru.ashalyapin.dto.CandidateUpdateDto;
import ru.ashalyapin.entity.CandidateEntity;
import ru.ashalyapin.event.CandidateCreatedEvent;

@Mapper(componentModel = "spring")
public interface CandidateMapper {
    CandidateEntity toEntity(CandidateDto candidateDto);
    CandidateEntity updateDtoToEntity(CandidateUpdateDto candidateDto);
    CandidateDto toDto(CandidateEntity candidateEntity);
    @Mapping(source = "id", target = "candidateId")
    CandidateCreatedEvent toEvent(CandidateEntity candidateEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget CandidateEntity entity, CandidateUpdateDto dto);
}
