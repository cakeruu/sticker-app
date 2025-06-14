package com.grup30.stickerapp.application.dto.forum.requests;

public record UnregisterMemberRequest(
        int memberId,
        int forumId
){}
