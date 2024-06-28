package com.youth.submitquiz.repository;

import com.youth.submitquiz.domain.Scorer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScorerRepository extends JpaRepository<Scorer, Long> {
    boolean existsByQuizId(Long quizId);
}
