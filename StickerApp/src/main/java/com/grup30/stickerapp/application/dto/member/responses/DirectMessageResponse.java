package com.grup30.stickerapp.application.dto.member.responses;

import java.time.LocalDateTime;

public record DirectMessageResponse(
        int id,
        int senderId,
        String senderFullName,
        int receiverId,
        String receiverFullName,
        boolean isRead,
        boolean isOnetime,
        String content,
        LocalDateTime sentAt
) { }
