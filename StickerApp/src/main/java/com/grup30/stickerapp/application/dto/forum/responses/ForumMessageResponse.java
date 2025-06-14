package com.grup30.stickerapp.application.dto.forum.responses;
import java.time.LocalDateTime;

public record ForumMessageResponse(
        int id,
        int forumId,
        int senderId,
        String senderFullName,
        String content,
        LocalDateTime sentAt
) { }
