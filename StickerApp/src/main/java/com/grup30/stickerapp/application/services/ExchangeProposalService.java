package com.grup30.stickerapp.application.services;

import com.grup30.stickerapp.application.dto.exchangeProposal.requests.CreateExchangeProposalRequest;
import com.grup30.stickerapp.application.dto.exchangeProposal.responses.ExchangeProposalOperationResponse;
import com.grup30.stickerapp.application.exceptions.BadRequestException;
import com.grup30.stickerapp.domain.Collection;
import com.grup30.stickerapp.domain.ExchangeProposal;
import com.grup30.stickerapp.domain.Sticker;
import com.grup30.stickerapp.domain.enums.ExchangeProposalStatus;
import com.grup30.stickerapp.persistence.ExchangeProposalRepository;
import com.grup30.stickerapp.persistence.MemberRepository;
import com.grup30.stickerapp.persistence.StickerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExchangeProposalService {
    private final ExchangeProposalRepository exchangeProposalRepository;
    private final StickerRepository stickerRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public ExchangeProposalService(ExchangeProposalRepository exchangeProposalRepository, StickerRepository stickerRepository, MemberRepository memberRepository) {
        this.exchangeProposalRepository = exchangeProposalRepository;
        this.stickerRepository = stickerRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public ExchangeProposalOperationResponse createExchangeProposal(Integer senderId, CreateExchangeProposalRequest req) {

        var sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new BadRequestException("Sender not found"));
        var receiver = memberRepository.findById(req.receiverId())
                .orElseThrow(() -> new BadRequestException("Receiver not found"));

        if (sender.equals(receiver)) {
            throw new BadRequestException("Sender and receiver cannot be the same member");
        }

        List<Sticker> offeredStickers = validateStickersInCollections(
                req.offeredStickerIds(),
                sender.getCollections(),
                "Offered"
        );

        List<Sticker> wantedStickers = validateStickersInCollections(
                req.wantedStickerIds(),
                receiver.getCollections(),
                "Wanted"
        );

        ExchangeProposal exchangeProposal = new ExchangeProposal(sender, receiver);

        offeredStickers.forEach(exchangeProposal::addOfferedSticker);
        wantedStickers.forEach(exchangeProposal::addWantedSticker);

        exchangeProposalRepository.save(exchangeProposal);

        return new ExchangeProposalOperationResponse(
                exchangeProposal.getId(),
                "Exchange proposal created successfully"
        );
    }

    @Transactional
    public ExchangeProposalOperationResponse acceptExchangeProposal(Integer memberId, Integer id) {
        ExchangeProposal exchangeProposal = exchangeProposalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exchange proposal not found "));

        if (!exchangeProposal.getReceiver().getId().equals(memberId)) throw new BadRequestException("Member is not the receiver of the Exchange proposal");

        finalizeTransaction(exchangeProposal);

        exchangeProposal.setStatus(ExchangeProposalStatus.ACCEPTED);

        return new ExchangeProposalOperationResponse(
                exchangeProposal.getId(),
                "Exchange proposal accepted successfully"
        );
    }

    private void finalizeTransaction(ExchangeProposal exchangeProposal) {

        var senderCollections = exchangeProposal.getSender().getCollections();
        var receiverCollections = exchangeProposal.getReceiver().getCollections();

        exchangeProposal.getOfferedStickers().forEach(sticker -> {
            exchangeStickers(sticker, senderCollections, receiverCollections);
        });

        exchangeProposal.getWantedStickers().forEach(sticker -> {
            exchangeStickers(sticker, receiverCollections, senderCollections);
        });
    }

    private void exchangeStickers(
            Sticker sticker,
            List<Collection> stickerRemovalCollections,
            List<Collection> stickerAdditionCollections
    ) {
        stickerRemovalCollections.forEach(senderCollection -> {
            // check if the sticker is in the collection
            if (senderCollection.getStickersCollected().contains(sticker)) {

                senderCollection.removeSticker(sticker);

                var collectionAlbum = senderCollection.getAlbum();

                stickerAdditionCollections.forEach(receiverCollection -> {
                    if (receiverCollection.getAlbum().equals(collectionAlbum)) {
                        receiverCollection.addSticker(sticker);
                    }
                });
            }
        });
    }

    @Transactional
    public ExchangeProposalOperationResponse rejectExchangeProposal(Integer memberId, Integer id) {
        ExchangeProposal exchangeProposal = exchangeProposalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exchange proposal not found "));

        if (!exchangeProposal.getReceiver().getId().equals(memberId)) throw new BadRequestException("Member is not the receiver of the exchange proposal");

        exchangeProposal.setStatus(ExchangeProposalStatus.REJECTED);

        return new ExchangeProposalOperationResponse(
                exchangeProposal.getId(),
                "Exchange proposal rejected successfully"
        );
    }

    @Transactional
    public ExchangeProposalOperationResponse cancelExchangeProposal(Integer memberId, Integer id) {
        ExchangeProposal exchangeProposal = exchangeProposalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exchange proposal not found "));

        if (!exchangeProposal.getSender().getId().equals(memberId)) throw new BadRequestException("Member is not the sender of the exchange proposal");

        exchangeProposal.setStatus(ExchangeProposalStatus.CANCELLED);

        return new ExchangeProposalOperationResponse(
                exchangeProposal.getId(),
                "Exchange proposal cancelled successfully"
        );
    }


    // private functions
    private List<Sticker> validateStickersInCollections(
            List<Integer> stickerIds,
            List<Collection> memberCollections,
            String stickerType) {

        if (stickerIds == null || stickerIds.isEmpty()) {
            return new ArrayList<>();
        }

        Set<Sticker> validStickers = memberCollections.stream()
                .flatMap(collection -> collection.getStickersCollected().stream())
                .collect(Collectors.toSet());

        List<Sticker> foundStickers = stickerRepository.findAllById(stickerIds);

        if (foundStickers.size() != stickerIds.size()) {
            throw new BadRequestException(stickerType + " stickers not found");
        }

        // Validate each sticker belongs to sender's or receiver's collections
        List<Sticker> invalidStickers = foundStickers.stream()
                .filter(sticker -> !validStickers.contains(sticker))
                .toList();

        if (!invalidStickers.isEmpty()) {
            throw new BadRequestException(
                    stickerType + " stickers do not belong to the member's collections"
            );
        }

        return foundStickers;
    }
}