package com.grup30.stickerapp.application.dto.exchangeProposal.responses;

import com.grup30.stickerapp.domain.Member;
import com.grup30.stickerapp.domain.Sticker;

import java.util.List;

public record GetExchangeProposalResponse(
        Integer id,
        Integer senderId,
        Integer receiverId,
        boolean senderAccepted,
        boolean receiverAccepted,
        List<Integer> offeredStickerIds,
        List<Integer> wantedStickerIds
) {

}
