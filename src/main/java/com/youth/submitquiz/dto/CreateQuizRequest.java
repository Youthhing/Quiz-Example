package com.youth.submitquiz.dto;

import java.util.List;

public record CreateQuizRequest(
        List<QuizRequest> quizzes
) {
}
