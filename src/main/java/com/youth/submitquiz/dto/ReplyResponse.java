package com.youth.submitquiz.dto;

public record ReplyResponse(
        Boolean result,
        Long memberId,
        Long ticketNumber
) {
    public static ReplyResponse of(boolean isAnswer, Long memberId, Long ticketNumber) {
        return new ReplyResponse(isAnswer, memberId, ticketNumber);
    }
}
