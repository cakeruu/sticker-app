package com.grup30.stickerapp.application.dto.member.requests;

import java.time.LocalDateTime;

public record UpdateMemberRequest(
        String password,
        String name,
        String secondName,
        String email,
        LocalDateTime dateOfBirth
) { }
