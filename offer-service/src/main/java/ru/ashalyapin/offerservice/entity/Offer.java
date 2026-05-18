package ru.ashalyapin.offerservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.ashalyapin.offerservice.model.OfferHistory;
import ru.ashalyapin.offerservice.model.OfferStatus;

import java.time.Instant;
import java.util.List;

@Document(collection = "offers")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Offer {

    @Id
    private String id;

    @Indexed
    private Long candidateId;

    @Indexed(unique = true)
    private String eventId;

    private OfferStatus status;

    private Integer recommendedSalary;
    private Integer finalSalary;

    private String grade;
    private Integer experienceYears;

    private Integer currentSalary;

    private Instant createdAt;

    private List<OfferHistory> history;
}
