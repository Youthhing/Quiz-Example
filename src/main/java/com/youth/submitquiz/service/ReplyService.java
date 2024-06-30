package com.youth.submitquiz.service;

import com.youth.submitquiz.domain.Member;
import com.youth.submitquiz.domain.Quiz;
import com.youth.submitquiz.domain.Reply;
import com.youth.submitquiz.domain.Scorer;
import com.youth.submitquiz.dto.ReplyResponse;
import com.youth.submitquiz.dto.SubmitAnswerRequest;
import com.youth.submitquiz.redis.ScorerExistRepository;
import com.youth.submitquiz.redis.TicketNumberRepository;
import com.youth.submitquiz.repository.MemberRepository;
import com.youth.submitquiz.repository.QuizRepository;
import com.youth.submitquiz.repository.ReplyRepository;
import com.youth.submitquiz.repository.ScorerRepository;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
    private final ScorerRepository scorerRepository;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;
    private final TicketNumberRepository ticketNumberRepository;
    private final ScorerExistRepository scorerExistRepository;

    @Transactional
    public ReplyResponse submitAnswer(SubmitAnswerRequest request) {
        Long ticketNumber = ticketNumberRepository.increment(request.quizId());
        log.info("[티켓 번호]: {}, [요청 시간] : {}", ticketNumber, LocalDateTime.now());

        Quiz findQuiz = quizRepository.findById(request.quizId()).orElseThrow();
        Member findMember = memberRepository.findById(request.memberId()).orElseThrow();

        boolean isAnswer = isAnswer(findQuiz, request.answer());

        // 정답이면 Redis에 Scorer의 ticketNumber 값을 저장하라  -> 이후에 느린 요청이 오더라도 컷 당함.
        if (isAnswer && scorerExistRepository.saveScorerIfFastest(findQuiz, ticketNumber)) {
            scorerRepository.findByQuizId(findQuiz.getId()).ifPresentOrElse(
                    scorer -> {
                        scorer.updateMember(findMember.getId());
                        scorerRepository.save(scorer);
                    },
                    () -> scorerRepository.save(Scorer.builder()
                            .build())
            );
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
