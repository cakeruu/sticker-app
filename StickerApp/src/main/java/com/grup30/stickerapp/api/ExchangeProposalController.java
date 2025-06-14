package com.grup30.stickerapp.api;

import com.grup30.stickerapp.application.dto.exchangeProposal.requests.CreateExchangeProposalRequest;
import com.grup30.stickerapp.application.dto.exchangeProposal.responses.ExchangeProposalOperationResponse;
import com.grup30.stickerapp.domain.ExchangeProposal;
import com.grup30.stickerapp.application.services.ExchangeProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.grup30.stickerapp.utils.Utils.resolveMemberId;

@RestController
@RequestMapping("/api/exchange-proposals")
public class ExchangeProposalController {

    private final ExchangeProposalService exchangeProposalService;

    public ExchangeProposalController(ExchangeProposalService exchangeProposalService) {
        this.exchangeProposalService = exchangeProposalService;
    }

    @PostMapping
    public ResponseEntity<ExchangeProposalOperationResponse> createExchangeProposal(
            @RequestBody CreateExchangeProposalRequest exchangeProposal,
            @RequestParam(required = false) Integer memberId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(exchangeProposalService.createExchangeProposal(resolveMemberId(memberId), exchangeProposal));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<ExchangeProposalOperationResponse> acceptExchangeProposal(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(exchangeProposalService.acceptExchangeProposal(resolveMemberId(memberId), id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ExchangeProposalOperationResponse> rejectExchangeProposal(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(exchangeProposalService.rejectExchangeProposal(resolveMemberId(memberId), id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ExchangeProposalOperationResponse> cancelExchangeProposal(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(exchangeProposalService.cancelExchangeProposal(resolveMemberId(memberId), id));
    }
}