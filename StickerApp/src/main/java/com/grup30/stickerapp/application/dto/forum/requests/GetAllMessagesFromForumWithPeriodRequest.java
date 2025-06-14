package com.grup30.stickerapp.application.dto.forum.requests;

import java.time.LocalDateTime;

public record GetAllMessagesFromForumWithPeriodRequest(
        int forumId,
        LocalDateTime startDate
) { }
