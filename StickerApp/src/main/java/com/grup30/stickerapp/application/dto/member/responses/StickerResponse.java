package com.grup30.stickerapp.application.dto.member.responses;

public record StickerResponse(
        int id,
        int number,
        String name,
        String description,
        String image,
        int sectionId
) { }
