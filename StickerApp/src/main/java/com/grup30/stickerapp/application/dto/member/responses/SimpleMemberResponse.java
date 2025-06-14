package com.grup30.stickerapp.application.dto.member.responses;

import java.time.LocalDateTime;

public record SimpleMemberResponse(
        int id,
        String fullName,
        String email,
        LocalDateTime dateOfBirth,
        LocalDateTime dateOfRegistration
) { }
