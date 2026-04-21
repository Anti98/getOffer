package ru.ashalyapin.calculationservice.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import ru.ashalyapin.calculationservice.constants.RedisKeys;
import ru.ashalyapin.calculationservice.dto.CoefficientsDto;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import static ru.ashalyapin.calculationservice.constants.RedisKeys.PROCESSED_EVENT_PREFIX;
import static ru.ashalyapin.calculationservice.mapper.RedisMapperUtil.mapToDto;
import static ru.ashalyapin.calculationservice.mapper.RedisMapperUtil.toRedisMap;

@Repository
@RequiredArgsConstructor
public class RedisCoefficientRepository {

    private final StringRedisTemplate redisTemplate;

    public Optional<CoefficientsDto> find() {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(RedisKeys.COEFFICIENTS);

        if (map.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(mapToDto(map));
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    public void save(CoefficientsDto dto) {
        redisTemplate.opsForHash()
                .putAll(RedisKeys.COEFFICIENTS, toRedisMap(dto));
    }

    public boolean tryMarkProcessed(String eventId) {
        String key = PROCESSED_EVENT_PREFIX + eventId;

        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, "1", Duration.ofHours(1));

        return Boolean.TRUE.equals(success);
    }

    public void removeProcessedMark(String eventId) {
        String key = PROCESSED_EVENT_PREFIX + eventId;
        redisTemplate.delete(key);
    }
}