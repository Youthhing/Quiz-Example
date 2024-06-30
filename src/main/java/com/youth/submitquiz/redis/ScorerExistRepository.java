package com.youth.submitquiz.redis;

import com.youth.submitquiz.domain.Quiz;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScorerExistRepository {

    private static final String KEY_PREFIX = "$scorer_";
    private static final Long NONE_VALUE = Long.MAX_VALUE;
    private static final Integer SCORER_EXPIRATION = 60 * 24;
    private final RedisTemplate<String, Long> redisTemplate;


    public boolean saveScorerIfFastest(Quiz quiz, Long ticketNumber) {
        if (getScorerTicketNumber(quiz) > ticketNumber) {
            saveScorer(quiz, ticketNumber);
            return true;
        } else {
            return false;
        }
    }

    public void saveScorer(Quiz quiz, Long ticketNumber) {
        String quizKey = KEY_PREFIX + quiz.getId();
        redisTemplate.opsForValue().set(
                quizKey,
                ticketNumber,
                SCORER_EXPIRATION,
                TimeUnit.MINUTES
        );
    }

    public Long getScorerTicketNumber(Quiz quiz){
        String quizKey = KEY_PREFIX + quiz.getId();
        if (redisTemplate.opsForValue().get(quizKey) == null) {
            return NONE_VALUE;
        }
        return redisTemplate.opsForValue().get(quizKey);
    }
}
