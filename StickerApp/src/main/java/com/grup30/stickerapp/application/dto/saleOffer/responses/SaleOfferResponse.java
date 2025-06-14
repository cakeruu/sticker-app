package com.grup30.stickerapp.application.dto.saleOffer.responses;

import com.grup30.stickerapp.application.dto.member.responses.SimpleMemberResponse;
import com.grup30.stickerapp.domain.enums.SaleOfferStatus;

import java.math.BigDecimal;
import java.util.List;

public record SaleOfferResponse(
        int id,
        int collectionId,
        int stickerId,
        BigDecimal price,
        SaleOfferStatus status,
        String collectionOwnerFullName,
        String stickerName,
        List<SimpleMemberResponse> interestedMembers
) {
}
