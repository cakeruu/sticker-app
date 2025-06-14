package com.grup30.stickerapp.application.dto.member.responses;

import java.util.List;

public record CheckStickersInCollectionResponse(
        int albumId,
        int memberId,
        int collectionId,
        String albumName,
        List<SectionStickerCollectedGroupResponse> stickers
) { }