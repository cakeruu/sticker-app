package com.grup30.stickerapp.application.dto.member.responses;

import com.grup30.stickerapp.domain.Forum;

import java.time.LocalDateTime;
import java.util.List;

public record MemberWithForumsResponse(
        int id,
        String fullName,
        String email,
        LocalDateTime dateOfBirth,
        LocalDateTime dateOfRegistration,
        List<Forum> forums
) {
}
