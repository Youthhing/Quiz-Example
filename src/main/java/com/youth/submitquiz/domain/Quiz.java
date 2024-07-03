package com.youth.submitquiz.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quiz_number", nullable = false)
    private Long number;

    @Column(name = "quiz_answer")
    private Long answer;

    @Column(name = "scorer_id")
    private Long memberId;

    @Column(name = "scorer_ticket_number")
    private Long ticketNumber;

    @Version
    private Long version;

    @Builder
    public Quiz(Long number, Long answer) {
        this.number = number;
        this.answer = answer;
        this.ticketNumber = Long.MAX_VALUE;
    }

    public void updateScorer(Long memberId, Long ticketNumber){
        this.memberId = memberId;
        this.ticketNumber = ticketNumber;
    }
}
