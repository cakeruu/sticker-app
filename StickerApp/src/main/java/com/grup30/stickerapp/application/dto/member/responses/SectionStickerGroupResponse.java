package com.grup30.stickerapp.application.dto.member.responses;

import com.grup30.stickerapp.domain.Sticker;

import java.util.List;

public record SectionStickerGroupResponse(
        int sectionId,
        String sectionName,
        List<Sticker> stickers
) {}

