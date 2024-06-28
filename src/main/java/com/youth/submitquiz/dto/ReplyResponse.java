package com.youth.submitquiz.dto;

public record ReplyResponse(
        Boolean result
) {
    public static ReplyResponse from(boolean isAnswer) {
        return new ReplyResponse(isAnswer);
    }
}
