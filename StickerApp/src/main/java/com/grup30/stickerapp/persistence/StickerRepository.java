package com.grup30.stickerapp.persistence;

import com.grup30.stickerapp.domain.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StickerRepository extends JpaRepository<Sticker, Integer> {
}

