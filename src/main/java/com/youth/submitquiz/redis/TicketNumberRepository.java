package com.youth.submitquiz.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TicketNumberRepository {

    private static final String KEY_PREFIX = "$quiz_";
    private final RedisTemplate<String , String> redisTemplate;

    public Long increment(Long quizId){
        String key = KEY_PREFIX + quizId;
        return redisTemplate.opsForValue()
                .increment(key);
    }
}
