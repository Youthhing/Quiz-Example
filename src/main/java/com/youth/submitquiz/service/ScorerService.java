package com.youth.submitquiz.service;

import com.youth.submitquiz.domain.Member;
import com.youth.submitquiz.domain.Quiz;
import com.youth.submitquiz.domain.Scorer;
import com.youth.submitquiz.repository.ScorerRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScorerService {

    private final ScorerRepository scorerRepository;
    private final EntityManager entityManager;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void checkAndThenUpdate(Quiz quiz, Member member, Long ticketNumber) {
        Optional<Scorer> maybeScorer = scorerRepository.findByQuizId(quiz.getId());
        log.info("[영속성 컨텍스트]: {}, {}", entityManager, ticketNumber);
        maybeScorer.ifPresentOrElse(
                scorer -> {
                    if (scorer.getTicketNumber() > ticketNumber) {
                        log.info("기존 득점자, 지금 : {}, {}", scorer.getTicketNumber(), ticketNumber);
                        scorer.updateScorer(member.getId(), ticketNumber);
                        scorerRepository.saveAndFlush(scorer);
                    }
                },
                () -> {
                    log.info("득점자 생성 : {}", ticketNumber);
                    scorerRepository.saveAndFlush(Scorer.builder()
                            .ticketNumber(ticketNumber)
                            .memberId(member.getId())
                            .quizId(quiz.getId())
                            .build());
                    log.info("==== 생성 시간 ==== {}" , LocalDateTime.now());
                }
        );
    }
}
