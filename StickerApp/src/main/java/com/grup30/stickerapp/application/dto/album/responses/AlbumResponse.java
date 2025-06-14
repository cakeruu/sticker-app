package com.grup30.stickerapp.application.dto.album.responses;

public record AlbumResponse(
        Integer id,
        String name,
        String beginningDate,
        String endingDate,
        String editor,
        int ownerId,
        String ownerFullName,
        int forumId,
        boolean isPublic
) { }
