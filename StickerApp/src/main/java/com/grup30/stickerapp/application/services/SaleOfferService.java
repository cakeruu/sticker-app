package com.grup30.stickerapp.application.services;

import com.grup30.stickerapp.application.dto.member.responses.SimpleMemberResponse;
import com.grup30.stickerapp.application.dto.saleOffer.requests.PutStickerOnSaleRequest;
import com.grup30.stickerapp.application.dto.saleOffer.responses.SaleOfferOperation;
import com.grup30.stickerapp.application.dto.saleOffer.responses.SaleOfferResponse;
import com.grup30.stickerapp.application.exceptions.BadRequestException;
import com.grup30.stickerapp.application.exceptions.NotFoundException;
import com.grup30.stickerapp.domain.Collection;
import com.grup30.stickerapp.domain.Member;
import com.grup30.stickerapp.domain.SaleOffer;
import com.grup30.stickerapp.domain.Sticker;
import com.grup30.stickerapp.domain.enums.SaleOfferStatus;
import com.grup30.stickerapp.persistence.MemberRepository;
import com.grup30.stickerapp.persistence.SaleOfferRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleOfferService {
    private record CheckMemberOwningStickerResult(Collection collection, Sticker sticker) {
    }

    private final SaleOfferRepository saleOfferRepository;
    private final MemberRepository memberRepository;

    public SaleOfferService(SaleOfferRepository saleOfferRepository, MemberRepository memberRepository) {
        this.saleOfferRepository = saleOfferRepository;
        this.memberRepository = memberRepository;
    }

    public SaleOfferOperation putStickerOnSale(Integer memberId, PutStickerOnSaleRequest req) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("Member not found"));
        var results = checkIfMemberOwnsSticker(member, req.collectionId(), req.stickerId());

        var saleOfferOptional = saleOfferRepository.findByStickerIdAndStatusIsActive(req.stickerId());

        if (saleOfferOptional.isPresent()) {
            throw new BadRequestException("Sticker is already on sale");
        }

        var saleOffer = new SaleOffer(results.collection, results.sticker, req.price(), member);

        saleOfferRepository.save(saleOffer);
        return new SaleOfferOperation(
                saleOffer.getId(),
                "Sticker is now on sale!"
        );
    }

    private CheckMemberOwningStickerResult checkIfMemberOwnsSticker(Member member, Integer collectionId, Integer stickerId) {
        var collections = member.getCollections();
        Collection collection = collections.stream()
                .filter(c -> c.getId().equals(collectionId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Collection not found"));

        var sticker = collection.getStickersCollected().stream()
                .filter(s -> s.getId().equals(stickerId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Sticker not found"));

        return new CheckMemberOwningStickerResult(collection, sticker);
    }

    @Transactional
    public SaleOfferOperation requestToBuySticker(Integer memberId, Integer stickerId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("Member not found"));

        SaleOffer saleOffer = saleOfferRepository.findByStickerIdAndStatusIsActive(stickerId).orElseThrow(() -> new NotFoundException("Sale offer not found"));

        saleOffer.setNewInterestedMember(member);
        return new SaleOfferOperation(
                saleOffer.getId(),
                "Request to buy sticker sent!"
        );
    }

    @Transactional
    public SaleOfferOperation acceptRequestToBuySticker(Integer memberId, Integer stickerId, Integer buyerId) {

        SaleOffer saleOffer = saleOfferRepository.findByStickerIdAndStatusIsActive(stickerId).orElseThrow(() -> new NotFoundException("Sale offer not found"));

        if (!saleOffer.getMember().getId().equals(memberId)) {
            throw new NotFoundException("Sale offer not found");
        }

        var collection = saleOffer.getCollection();
        var sticker = saleOffer.getSticker();

        var buyer = saleOffer.getInterestedMembers().stream()
                .filter(m -> m.getId().equals(buyerId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Buyer not found"));
        var buyerCollection = buyer.getCollections().stream()
                .filter(c -> c.getAlbum().getId().equals(collection.getAlbum().getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Buyer collection not found"));
        buyerCollection.addSticker(sticker);
        saleOffer.setStatus(SaleOfferStatus.SOLD);

        saleOffer.getInterestedMembers()
                .removeIf(m -> m.getId().equals(buyerId));

        collection.getStickersCollected().removeIf(s -> s.getId().equals(stickerId));

        return new SaleOfferOperation(
                saleOffer.getId(),
                "Sticker sold successfully!"
        );
    }


    @Transactional
    public SaleOfferOperation rejectRequestToBuySticker(Integer memberId, Integer stickerId,  Integer buyerId) {

        SaleOffer saleOffer = saleOfferRepository.findByStickerIdAndStatusIsActive(stickerId).orElseThrow(() -> new NotFoundException("Sale offer not found"));

        if (!saleOffer.getMember().getId().equals(memberId)) {
            throw new NotFoundException("Sale offer not found");
        }

        saleOffer.getInterestedMembers()
                .removeIf(m -> m.getId().equals(buyerId));

        return new SaleOfferOperation(
                saleOffer.getId(),
                "Offer rejected"
        );
    }

    public List<SaleOfferResponse> getAllSaleOffers(Integer memberId) {
        var saleOffers = saleOfferRepository.findAllByMemberAndStatusIsActive(memberId);

        return saleOffers.stream()
                .map(so -> new SaleOfferResponse(
                        so.getId(),
                        so.getCollection().getId(),
                        so.getSticker().getId(),
                        so.getPrice(),
                        so.getStatus(),
                        so.getMember().getName() + " " + so.getMember().getSecondName(),
                        so.getSticker().getName(),
                        so.getInterestedMembers().stream()
                                .map(m -> new SimpleMemberResponse(
                                        m.getId(),
                                        m.getName() + " " + m.getSecondName(),
                                        m.getEmail(),
                                        m.getDateOfBirth(),
                                        m.getDateOfRegistration()
                                )).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}
