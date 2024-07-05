package com.youth.submitquiz.repository;

import com.youth.submitquiz.domain.Scorer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScorerRepository extends JpaRepository<Scorer, Long> {
    boolean existsByQuizId(Long quizId);

    Optional<Scorer> findByQuizId(Long quizId);
}
