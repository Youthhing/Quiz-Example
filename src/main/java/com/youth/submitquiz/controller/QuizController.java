package com.youth.submitquiz.controller;

import com.youth.submitquiz.dto.CreateQuizRequest;
import com.youth.submitquiz.dto.FindQuizResponse;
import com.youth.submitquiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<Void> createQuiz(@RequestBody CreateQuizRequest request){
        quizService.uploadQuiz(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindQuizResponse> getQuiz(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(quizService.findQuiz(id));
    }
}
