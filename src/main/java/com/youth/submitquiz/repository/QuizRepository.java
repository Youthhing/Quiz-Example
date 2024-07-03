package com.youth.submitquiz.repository;

import com.youth.submitquiz.domain.Quiz;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select q from Quiz q where q.id = :id")
    Optional<Quiz> findByIdWithOptimisticLock(Long id);
}
