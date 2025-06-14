package com.grup30.stickerapp.api;

import com.grup30.stickerapp.application.dto.saleOffer.requests.PutStickerOnSaleRequest;
import com.grup30.stickerapp.application.dto.saleOffer.responses.SaleOfferOperation;
import com.grup30.stickerapp.application.dto.saleOffer.responses.SaleOfferResponse;
import com.grup30.stickerapp.application.services.SaleOfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.grup30.stickerapp.utils.Utils.*;

@RestController
@RequestMapping("/api/sale-offer")
public class SaleOfferController {

    private final SaleOfferService saleOfferService;
    public SaleOfferController(SaleOfferService saleOfferService) {
        this.saleOfferService = saleOfferService;
    }

    @PostMapping
    public ResponseEntity<SaleOfferOperation> putStickerOnSale(
            @RequestParam(required = false) Integer memberId,
            @RequestBody PutStickerOnSaleRequest req) {
        return ResponseEntity.ok(saleOfferService.putStickerOnSale(resolveMemberId(memberId), req));
    }

    @PostMapping("/{stickerId}/request")
    public ResponseEntity<SaleOfferOperation> requestToBuySticker(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer stickerId) {
        return ResponseEntity.ok(saleOfferService.requestToBuySticker(resolveMemberId(memberId), stickerId));
    }

    @PutMapping("/{stickerId}/accept/{buyerId}")
    public ResponseEntity<SaleOfferOperation> acceptRequestToBuySticker(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer stickerId,
            @PathVariable Integer buyerId
    ) {
        return ResponseEntity.ok(saleOfferService.acceptRequestToBuySticker(resolveMemberId(memberId), stickerId, buyerId));
    }

    @PutMapping("/{stickerId}/reject/{buyerId}")
    public ResponseEntity<SaleOfferOperation> rejectRequestToBuySticker(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer stickerId,
            @PathVariable Integer buyerId
    ) {
        return ResponseEntity.ok(saleOfferService.rejectRequestToBuySticker(resolveMemberId(memberId), stickerId, buyerId));
    }

    @GetMapping
    public ResponseEntity<List<SaleOfferResponse>> getAllSaleOffers(
            @RequestParam(required = false) Integer memberId
    ) {
        return ResponseEntity.ok(saleOfferService.getAllSaleOffers(resolveMemberId(memberId)));
    }
}
