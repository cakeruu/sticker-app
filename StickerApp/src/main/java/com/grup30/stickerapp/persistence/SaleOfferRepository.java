package com.grup30.stickerapp.persistence;

import com.grup30.stickerapp.domain.Member;
import com.grup30.stickerapp.domain.SaleOffer;
import com.grup30.stickerapp.domain.enums.SaleOfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleOfferRepository extends JpaRepository<SaleOffer, Integer> {

    @Query("SELECT so FROM SaleOffer so " +
            "WHERE so.sticker.id = :stickerId " +
            "AND so.status = com.grup30.stickerapp.domain.enums.SaleOfferStatus.ACTIVE")
    Optional<SaleOffer> findByStickerIdAndStatusIsActive(@Param("stickerId") Integer stickerId);

    @Query("SELECT so FROM SaleOffer so " +
            "WHERE so.member.id = :memberId " +
            "AND so.status = com.grup30.stickerapp.domain.enums.SaleOfferStatus.ACTIVE")
    List<SaleOffer> findAllByMemberAndStatusIsActive(@Param("memberId") Integer memberId);
}