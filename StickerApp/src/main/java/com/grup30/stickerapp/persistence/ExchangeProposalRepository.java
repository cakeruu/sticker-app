package com.grup30.stickerapp.persistence;

import com.grup30.stickerapp.domain.Collection;
import com.grup30.stickerapp.domain.ExchangeProposal;
import com.grup30.stickerapp.domain.Member;
import com.grup30.stickerapp.domain.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExchangeProposalRepository extends JpaRepository<ExchangeProposal, Integer> {
}