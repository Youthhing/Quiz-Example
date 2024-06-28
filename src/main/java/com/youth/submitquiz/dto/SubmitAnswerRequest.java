package com.youth.submitquiz.dto;

public record SubmitAnswerRequest(
        Long quizId,
        Long memberId,
        Long answer
) {
}
