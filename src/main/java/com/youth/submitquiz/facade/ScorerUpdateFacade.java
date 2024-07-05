package com.youth.submitquiz.facade;

import com.youth.submitquiz.domain.Member;
import com.youth.submitquiz.domain.Quiz;
import com.youth.submitquiz.service.ScorerService;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScorerUpdateFacade {

    private static final String KEY_PREFIX = "$Scorer_redisson_lock";
    private final RedissonClient redissonClient;
    private final ScorerService scorerService;

    public void checkAndUpdateScorer(Quiz quiz, Member member, Long ticketNumber) {
        RLock lock = redissonClient.getLock(generate(quiz.getId()));

        try {
            boolean available = lock.tryLock(30, 1, TimeUnit.SECONDS);

            if (!available) {
                log.warn("[락 획득 실패] : {}", ticketNumber);
                return;
            }
            log.info("[락 획득 : {}], 시간: {}", ticketNumber, LocalDateTime.now());
            scorerService.checkAndThenUpdate(quiz, member, ticketNumber);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                log.info("[락 반납 : {}]", ticketNumber);
                lock.unlock();
            }
        }
    }

    private String generate(Long id) {
        return KEY_PREFIX + id;
    }
}
