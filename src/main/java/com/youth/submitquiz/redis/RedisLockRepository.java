package com.youth.submitquiz.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {

    private static final String KEY_PREFIX = "$Scorer_lock";
    private final RedisTemplate<String, String> redisTemplate;

    public Boolean lock(Long key){
        return redisTemplate.opsForValue()
                .setIfAbsent(generate(key), "lock");
    }

    public Boolean unlock(Long key){
        return redisTemplate.delete(generate(key));
    }

    private String generate(Long key) {
        return KEY_PREFIX + key;
    }
}
