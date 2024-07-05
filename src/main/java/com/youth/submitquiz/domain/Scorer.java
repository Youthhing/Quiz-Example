package com.youth.submitquiz.domain;

import com.youth.submitquiz.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scorer extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quiz_id", nullable = false, unique = true)
    private Long quizId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "ticket_number", nullable = false)
    private Long ticketNumber;

    @Builder
    public Scorer(final Long quizId, Long memberId, Long ticketNumber) {
        this.quizId = quizId;
        this.memberId = memberId;
        this.ticketNumber = ticketNumber;
    }

    public void updateScorer(Long memberId, Long ticketNumber) {
        this.memberId = memberId;
        this.ticketNumber = ticketNumber;
    }
}
