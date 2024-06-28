package com.youth.submitquiz.service;

import com.youth.submitquiz.domain.Member;
import com.youth.submitquiz.domain.Quiz;
import com.youth.submitquiz.domain.Reply;
import com.youth.submitquiz.domain.Scorer;
import com.youth.submitquiz.dto.ReplyResponse;
import com.youth.submitquiz.dto.SubmitAnswerRequest;
import com.youth.submitquiz.repository.MemberRepository;
import com.youth.submitquiz.repository.QuizRepository;
import com.youth.submitquiz.repository.ReplyRepository;
import com.youth.submitquiz.repository.ScorerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyService {

    private final QuizRepository quizRepository;
    private final ScorerRepository scorerRepository;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public ReplyResponse submitAnswer(SubmitAnswerRequest request) {
        Quiz findQuiz = quizRepository.findById(request.quizId()).orElseThrow();

        boolean isAnswer = isAnswer(findQuiz, request.answer());

        if (isAnswer && !scorerRepository.existsByQuizId(findQuiz.getId())) {
            scorerRepository.save(Scorer.builder()
                    .quizId(findQuiz.getId())
                    .memberId(request.memberId())
                    .build());
        }

        Member findMember = memberRepository.findById(request.memberId()).orElseThrow();

        replyRepository.save(Reply.builder()
                .quiz(findQuiz)
                .memberId(findMember.getId())
                .build());

        return ReplyResponse.from(isAnswer);
    }

    private boolean isAnswer(Quiz quiz, Long answer) {
        return quiz.getAnswer().equals(answer);
    }
}
