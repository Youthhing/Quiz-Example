package com.youth.submitquiz.service;

import com.youth.submitquiz.domain.Member;
import com.youth.submitquiz.domain.Quiz;
import com.youth.submitquiz.domain.Scorer;
import com.youth.submitquiz.repository.ScorerRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScorerService {

    private final ScorerRepository scorerRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void checkAndThenUpdateScorer(Quiz findQuiz, Member findMember, Long ticketNumber) {
        Optional<Scorer> maybeScorer = scorerRepository.findByQuizId(findQuiz.getId());

        maybeScorer.ifPresentOrElse(
                scorer -> {
                    if (scorer.getTicketNumber() > ticketNumber) {
                        scorer.updateScorer(findMember.getId(), ticketNumber);
                        scorerRepository.saveAndFlush(scorer);
                    }
                },
                () -> {
                    scorerRepository.saveAndFlush(Scorer.builder()
                            .quizId(findQuiz.getId())
                            .memberId(findMember.getId())
                            .ticketNumber(ticketNumber)
                            .build());
                }
        );
    }
}
