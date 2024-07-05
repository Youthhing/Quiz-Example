package com.youth.submitquiz.service;

import com.youth.submitquiz.domain.Member;
import com.youth.submitquiz.domain.Quiz;
import com.youth.submitquiz.domain.Reply;
import com.youth.submitquiz.dto.ReplyResponse;
import com.youth.submitquiz.dto.SubmitAnswerRequest;
import com.youth.submitquiz.facade.LettuceLockScorerUpdateFacade;
import com.youth.submitquiz.redis.TicketNumberRepository;
import com.youth.submitquiz.repository.MemberRepository;
import com.youth.submitquiz.repository.QuizRepository;
import com.youth.submitquiz.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyService {

    private final QuizRepository quizRepository;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;
    private final TicketNumberRepository ticketNumberRepository;
    private final LettuceLockScorerUpdateFacade lettuceLockScorerUpdateFacade;

    @Transactional
    public ReplyResponse submitAnswer(SubmitAnswerRequest request) throws InterruptedException {
        Long ticketNumber = ticketNumberRepository.increment(request.quizId());
        Quiz findQuiz = quizRepository.findById(request.quizId()).orElseThrow();
        Member findMember = memberRepository.findById(request.memberId()).orElseThrow();

        boolean isAnswer = isAnswer(findQuiz, request.answer());

        if (isAnswer) {
            lettuceLockScorerUpdateFacade.checkAndThenUpdate(findQuiz, findMember, ticketNumber);
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
