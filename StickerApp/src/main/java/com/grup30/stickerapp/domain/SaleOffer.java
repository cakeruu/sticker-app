package com.grup30.stickerapp.domain;

import com.grup30.stickerapp.domain.enums.SaleOfferStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sale_offer")
public class SaleOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    @ManyToOne
    @JoinColumn(name = "sticker_id", nullable = false)
    private Sticker sticker;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SaleOfferStatus status;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "sale_offer_member",
            joinColumns = @JoinColumn(name = "sale_offer_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<Member> interestedMembers;

    public SaleOffer() {}

    public SaleOffer(Collection collection, Sticker sticker, BigDecimal price, Member member) {
        this.collection = collection;
        this.sticker = sticker;
        this.price = price;
        this.createdAt = LocalDateTime.now();
        this.status = SaleOfferStatus.ACTIVE;
        this.member = member;
        interestedMembers = List.of();
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public Collection getCollection() {
        return collection;
    }

    public Sticker getSticker() {
        return sticker;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public SaleOfferStatus getStatus() {
        return status;
    }

    public void setNewInterestedMember(Member member) {
        interestedMembers.add(member);
    }
    public void setStatus(SaleOfferStatus status) {
        this.status = status;
    }

    public List<Member> getInterestedMembers() {
        return interestedMembers;
    }

    public Member getMember() {
        return member;
    }
}
