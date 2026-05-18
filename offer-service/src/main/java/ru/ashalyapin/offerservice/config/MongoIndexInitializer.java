package ru.ashalyapin.offerservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import ru.ashalyapin.offerservice.entity.Offer;

@Configuration
@RequiredArgsConstructor
public class MongoIndexInitializer {

    private final MongoTemplate mongoTemplate;

    @Bean
    ApplicationRunner ensureMongoIndexes() {
        return args -> {
            var indexOps = mongoTemplate.indexOps(Offer.class);
            indexOps.ensureIndex(new Index().on("candidateId", Sort.Direction.ASC));
            indexOps.ensureIndex(new Index().on("eventId", Sort.Direction.ASC).unique());
        };
    }
}
