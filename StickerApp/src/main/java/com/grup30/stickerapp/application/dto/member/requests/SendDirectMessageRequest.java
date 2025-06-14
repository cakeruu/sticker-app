package com.grup30.stickerapp.application.dto.member.requests;

import com.grup30.stickerapp.domain.DirectMessage;
import com.grup30.stickerapp.domain.Forum;
import com.grup30.stickerapp.domain.Member;

import java.time.LocalDateTime;

public record SendDirectMessageRequest(
        int receiverId,
        boolean isOnetime,
        String content
) {
    public DirectMessage toDirectMessage(Member sender, Member receiver, Forum forum) {
        return new DirectMessage(
                this.content(),
                sender,
                receiver,
                LocalDateTime.now(),
                forum,
                this.isOnetime
        );
    }
}
