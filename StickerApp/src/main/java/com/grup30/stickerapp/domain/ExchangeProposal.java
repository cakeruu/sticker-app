package com.grup30.stickerapp.domain;

import com.grup30.stickerapp.domain.enums.ExchangeProposalStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ExchangeProposal")
public class ExchangeProposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sender")
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "receiver")
    private Member receiver;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExchangeProposalStatus status;

    @ManyToMany
    @JoinTable(
            name = "exchange_proposal_offered_stickers",
            joinColumns = @JoinColumn(name = "exchange_proposal_id"),
            inverseJoinColumns = @JoinColumn(name = "sticker_id")
    )
    private List<Sticker> offeredStickers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "exchange_proposal_wanted_stickers",
            joinColumns = @JoinColumn(name = "exchange_proposal_id"),
            inverseJoinColumns = @JoinColumn(name = "sticker_id")
    )
    private List<Sticker> wantedStickers = new ArrayList<>();

    public ExchangeProposal() {}

    public ExchangeProposal(Member sender, Member receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = ExchangeProposalStatus.PENDING;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public Member getSender() {
        return sender;
    }

    public Member getReceiver() {
        return receiver;
    }

    public ExchangeProposalStatus getStatus() {
        return status;
    }

    public List<Sticker> getOfferedStickers() {
        return offeredStickers;
    }

    public List<Sticker> getWantedStickers() {
        return wantedStickers;
    }

    // Convenience methods to add stickers
    public void addOfferedSticker(Sticker sticker) {
        if (!this.offeredStickers.contains(sticker)) {
            this.offeredStickers.add(sticker);
        }
    }

    public void addWantedSticker(Sticker sticker) {
        if (!this.wantedStickers.contains(sticker)) {
            this.wantedStickers.add(sticker);
        }
    }

    public void setStatus(ExchangeProposalStatus exchangeProposalStatus) {
        this.status = exchangeProposalStatus;
    }
}