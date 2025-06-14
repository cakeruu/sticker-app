package com.grup30.stickerapp.application.dto.member.responses;

import com.grup30.stickerapp.application.dto.album.responses.AlbumResponse;

public record SimpleCollectionResponse(
        int id,
        int memberId,
        AlbumResponse album
)
{}
