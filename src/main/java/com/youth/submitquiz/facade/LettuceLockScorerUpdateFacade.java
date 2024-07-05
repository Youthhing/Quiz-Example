package com.youth.submitquiz.facade;

import com.youth.submitquiz.domain.Member;
import com.youth.submitquiz.domain.Quiz;
import com.youth.submitquiz.redis.RedisLockRepository;
import com.youth.submitquiz.service.ScorerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LettuceLockScorerUpdateFacade {

    private final RedisLockRepository redisLockRepository;
    private final ScorerService scorerService;

    public void checkAndThenUpdate(Quiz findQuiz, Member findMember, Long ticketNumber) throws InterruptedException {
        while (!redisLockRepository.lock(findQuiz.getId())) {
            Thread.sleep(100);
        }

        try {
            scorerService.checkAndThenUpdateScorer(findQuiz, findMember, ticketNumber);
        } finally {
            redisLockRepository.unlock(findQuiz.getId());
        }
    }
}
