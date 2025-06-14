package com.grup30.stickerapp.application.dto.forum.responses;

public record RegisterMemberResponse(
        int id,
        int forumId,
        String message
) { }
