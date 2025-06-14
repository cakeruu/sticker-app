package com.grup30.stickerapp.application.dto.member.responses;

public record BlockMemberResponse(
        Integer userBlockedId,
        String message
) { }