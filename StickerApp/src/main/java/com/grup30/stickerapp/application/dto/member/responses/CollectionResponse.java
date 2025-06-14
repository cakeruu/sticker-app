package com.grup30.stickerapp.application.dto.member.responses;

import java.util.List;

public record CollectionResponse(
        int id,
        int memberId,
        int albumId,
        String albumName,
        List<SectionStickerGroupResponse> stickers
) { }
