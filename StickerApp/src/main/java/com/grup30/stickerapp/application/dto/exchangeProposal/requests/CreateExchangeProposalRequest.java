package com.grup30.stickerapp.application.dto.exchangeProposal.requests;

import java.util.List;

public record CreateExchangeProposalRequest(
        Integer receiverId,
        List<Integer> offeredStickerIds,
        List<Integer> wantedStickerIds
) { }