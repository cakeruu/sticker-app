package com.grup30.stickerapp.application.dto.album.responses;

public record StartAlbumCollectionResponse(
        int albumId,
        int memberId,
        int collectionId,
        String message
) { }
