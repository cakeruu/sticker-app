package com.grup30.stickerapp.application.dto.member.responses;

public record CollectStickerResponse(
        int memberId,
        int collectionId,
        int stickerId,
        String message
) { }
