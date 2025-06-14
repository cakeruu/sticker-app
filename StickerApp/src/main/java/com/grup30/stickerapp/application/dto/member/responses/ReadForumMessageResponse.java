package com.grup30.stickerapp.application.dto.member.responses;

public record ReadForumMessageResponse(
        int memberId,
        int forumId,
        int messageId,
        String message
) { }
