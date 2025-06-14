package com.grup30.stickerapp.application.dto.member.responses;

import com.grup30.stickerapp.domain.Sticker;

import java.util.List;

public record SectionStickerCollectedGroupResponse(
        int sectionId,
        String sectionName,
        List<Sticker> stickersCollected,
        List<Sticker> stickersNotCollected
) {}

