package com.youth.submitquiz.service;

import com.youth.submitquiz.domain.Quiz;
import com.youth.submitquiz.dto.CreateQuizRequest;
import com.youth.submitquiz.dto.FindQuizResponse;
import com.youth.submitquiz.redis.ScorerExistRepository;
import com.youth.submitquiz.repository.QuizRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizService {

    private final QuizRepository quizRepository;
    private final ScorerExistRepository scorerExistRepository;

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

    public void saveToCache() {
        List<Long> quizIds = quizRepository.findAll().stream()
                .map(Quiz::getId)
                .toList();
        quizIds.forEach(scorerExistRepository::setCache);
    }
}
