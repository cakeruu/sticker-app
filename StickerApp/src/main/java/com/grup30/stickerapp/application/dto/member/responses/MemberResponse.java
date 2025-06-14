package com.grup30.stickerapp.application.dto.member.responses;

import java.time.LocalDateTime;

public record MemberResponse(
        int id,
        String name,
        String secondName,
        String email,
        LocalDateTime dateOfBirth
) {
}
