package com.grup30.stickerapp.application.dto.forum.responses;

public record UnregisterMemberResponse(
        int id,
        int forumId,
        String message
) { }
