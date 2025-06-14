package com.grup30.stickerapp.application.dto.member.responses;

public record ReadDirectMessageResponse(
        int id,
        int senderId,
        int receiverId,
        String message
) { }
