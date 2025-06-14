package com.grup30.stickerapp.application.dto.saleOffer.requests;


import java.math.BigDecimal;
public record PutStickerOnSaleRequest(
    int collectionId,
    int stickerId,
    BigDecimal price
) {
}