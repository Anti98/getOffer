package ru.ashalyapin.offerservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ashalyapin.offerservice.entity.Offer;
import ru.ashalyapin.offerservice.model.OfferHistory;
import ru.ashalyapin.offerservice.model.OfferHistoryResponse;
import ru.ashalyapin.offerservice.model.OfferResponse;

import java.util.List;


@Mapper(componentModel = "spring")
public interface OfferMapper {

    @Mapping(target = "status", expression = "java(offer.getStatus().name())")
    OfferResponse toResponse(Offer offer);

    OfferHistoryResponse toHistory(OfferHistory history);

    List<OfferHistoryResponse> toHistoryList(List<OfferHistory> history);
}