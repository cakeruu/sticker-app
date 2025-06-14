package com.grup30.stickerapp.application.dto.forum.responses;

import java.time.LocalDateTime;

public record ForumResponse(
        int id,
        String albumName,
        int ownerId,
        LocalDateTime createdAt
) { }
