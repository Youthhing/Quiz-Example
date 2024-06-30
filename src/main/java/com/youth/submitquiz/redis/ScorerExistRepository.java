package com.youth.submitquiz.redis;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ScorerExistRepository {

    private static final String KEY_PREFIX = "$scorer_";
    private static final Long NONE_VALUE = Long.MAX_VALUE;
    private static final Integer SCORER_EXPIRATION = 60 * 24;
    private final RedisTemplate<String, Long> redisTemplate;


    public boolean saveScorerIfFastest(final Long quizId, Long ticketNumber) {
        if (getScorerTicketNumber(quizId) > ticketNumber) {
            log.info("[기존 득점자보다 빠른 요청 들어옴] 기존: {}, 현재: {}", getScorerTicketNumber(quizId), ticketNumber);
            saveScorer(quizId, ticketNumber);
            return true;
        } else {
            return false;
        }
    }

    public void saveScorer(Long quizId, Long ticketNumber) {
        String quizKey = KEY_PREFIX + quizId;
        redisTemplate.opsForValue().set(
                quizKey,
                ticketNumber,
                SCORER_EXPIRATION,
                TimeUnit.MINUTES
        );
    }

    public Long getScorerTicketNumber(final Long quizId){
        String quizKey = KEY_PREFIX + quizId;
        if (redisTemplate.opsForValue().get(quizKey) == null) {
            redisTemplate.opsForValue().set(quizKey, NONE_VALUE, SCORER_EXPIRATION, TimeUnit.MINUTES);
            return NONE_VALUE;
        }
        return redisTemplate.opsForValue().get(quizKey);
    }

    public void setCache(Long quizId) {
        String quizKey = KEY_PREFIX + quizId;
        redisTemplate.opsForValue().set(
                quizKey,
                NONE_VALUE,
                SCORER_EXPIRATION,
                TimeUnit.MINUTES
        );
    }
}
