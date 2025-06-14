package com.grup30.stickerapp.application.dto.forum.requests;

import com.grup30.stickerapp.domain.Forum;
import com.grup30.stickerapp.domain.Member;
import com.grup30.stickerapp.domain.ForumMessage;

import java.time.LocalDateTime;

public record SendForumMessageRequest(
        int forumId,
        String message
) {
    public ForumMessage toMessage(Member user, Forum forum, String message) {

        return new ForumMessage(
                forum,
                user,
                message,
                LocalDateTime.now()
        );
    }
}
