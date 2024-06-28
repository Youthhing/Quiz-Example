package com.youth.submitquiz.dto;

import com.youth.submitquiz.domain.Quiz;

public record FindQuizResponse(
        Long id,
        Long number
) {
    public static FindQuizResponse from(Quiz findQuiz) {
        return new FindQuizResponse(findQuiz.getId(), findQuiz.getNumber());
    }
}
