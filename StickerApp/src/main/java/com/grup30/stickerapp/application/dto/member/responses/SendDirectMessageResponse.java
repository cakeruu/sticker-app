package com.grup30.stickerapp.application.dto.member.responses;

import java.time.LocalDateTime;

public record SendDirectMessageResponse(
        int messageId,
        int senderId,
        int receiverId,
        LocalDateTime date,
        String message
) { }