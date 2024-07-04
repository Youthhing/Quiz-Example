package com.youth.submitquiz.repository;

import com.youth.submitquiz.domain.Scorer;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ScorerRepository extends JpaRepository<Scorer, Long> {
    boolean existsByQuizId(Long quizId);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Scorer s where s.quizId = :quizId")
    boolean existByQuizIdWithPessimisticLock(Long quizId);

    Optional<Scorer> findByQuizId(Long quizId);
}
