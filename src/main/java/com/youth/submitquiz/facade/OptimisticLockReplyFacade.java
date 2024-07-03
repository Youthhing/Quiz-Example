package com.youth.submitquiz.facade;

import com.youth.submitquiz.domain.Member;
import com.youth.submitquiz.domain.Quiz;
import com.youth.submitquiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptimisticLockReplyFacade {

    private final QuizService quizService;

    public void checkAndUpdateScorer(Quiz quiz, Member member, Long ticketNumber) throws InterruptedException {
        while (true) {
            try {
//                entityManager.flush();
                quizService.updateScorer(quiz, member, ticketNumber);
                break;
            } catch (Exception e){
                log.warn("[락 획득 대기]");
                Thread.sleep(50);
            }
        }
    }
}
