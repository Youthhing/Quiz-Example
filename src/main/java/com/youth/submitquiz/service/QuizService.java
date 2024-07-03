package com.youth.submitquiz.service;

import com.youth.submitquiz.domain.Member;
import com.youth.submitquiz.domain.Quiz;
import com.youth.submitquiz.dto.CreateQuizRequest;
import com.youth.submitquiz.dto.FindQuizResponse;
import com.youth.submitquiz.repository.QuizRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizService {

    private final QuizRepository quizRepository;

    @Transactional
    public void uploadQuiz(CreateQuizRequest request) {
        List<Quiz> quizzes = request.quizzes().stream()
                .map(req -> Quiz.builder()
                        .answer(req.answer())
                        .number(req.number())
                        .build())
                .toList();

        quizRepository.saveAll(quizzes);
    }

    public FindQuizResponse findQuiz(Long id) {
        Quiz findQuiz = quizRepository.findById(id).orElseThrow();
        return FindQuizResponse.from(findQuiz);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateScorer(Quiz quiz, Member member, Long ticketNumber) {
        Quiz findQuiz = quizRepository.findByIdWithOptimisticLock(quiz.getId()).orElseThrow();

        if (findQuiz.getTicketNumber() > ticketNumber) {
            findQuiz.updateScorer(member.getId(), ticketNumber);
            quizRepository.save(findQuiz);
            log.info("[득점자 업데이트] 기존: {}, 수정 후 {}", "", ticketNumber);
        }
    }
}
