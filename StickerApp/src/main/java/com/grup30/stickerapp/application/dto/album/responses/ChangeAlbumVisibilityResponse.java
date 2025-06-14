package com.grup30.stickerapp.application.dto.album.responses;

public record ChangeAlbumVisibilityResponse(
        int albumId,
        boolean isPublic,
        String message
) { }
