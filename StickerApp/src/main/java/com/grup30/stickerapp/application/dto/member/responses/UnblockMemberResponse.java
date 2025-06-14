package com.grup30.stickerapp.application.dto.member.responses;

public record UnblockMemberResponse(
        Integer userUnblockedId,
        String message
) { }
