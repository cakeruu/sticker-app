package com.grup30.stickerapp.application.dto.exchangeProposal.responses;

public record ExchangeProposalResponse(
        int senderId,
        String senderFullName,
        int receiverId,
        String receiverFullName
) { }