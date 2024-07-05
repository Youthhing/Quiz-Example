package com.youth.submitquiz.service;

import com.youth.submitquiz.domain.Member;
import com.youth.submitquiz.domain.Quiz;
import com.youth.submitquiz.domain.Reply;
import com.youth.submitquiz.dto.ReplyResponse;
import com.youth.submitquiz.dto.SubmitAnswerRequest;
import com.youth.submitquiz.facade.ScorerUpdateFacade;
import com.youth.submitquiz.redis.TicketNumberRepository;
import com.youth.submitquiz.repository.MemberRepository;
import com.youth.submitquiz.repository.QuizRepository;
import com.youth.submitquiz.repository.ReplyRepository;
import com.youth.submitquiz.repository.ScorerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyService {

    private final QuizRepository quizRepository;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;
    private final ScorerUpdateFacade scorerUpdateFacade;
    private final TicketNumberRepository ticketNumberRepository;

    @Transactional
    public ReplyResponse submitAnswer(SubmitAnswerRequest request) {
        Long ticketNumber = ticketNumberRepository.increment(request.quizId());
        Quiz findQuiz = quizRepository.findById(request.quizId()).orElseThrow();
        Member findMember = memberRepository.findById(request.memberId()).orElseThrow();

        boolean isAnswer = isAnswer(findQuiz, request.answer());

        if (isAnswer) {
            scorerUpdateFacade.checkAndUpdateScorer(findQuiz, findMember, ticketNumber);
        }

        replyRepository.save(Reply.builder()
                .quiz(findQuiz)
                .memberId(findMember.getId())
                .ticketNumber(ticketNumber)
                .build());

        return ReplyResponse.from(isAnswer);
    }

    private boolean isAnswer(Quiz quiz, Long answer) {
        return quiz.getAnswer().equals(answer);
    }
}
