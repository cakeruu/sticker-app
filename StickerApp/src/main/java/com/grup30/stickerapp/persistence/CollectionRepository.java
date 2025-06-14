package com.grup30.stickerapp.persistence;

import com.grup30.stickerapp.domain.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Integer> {
    Optional<List<Collection>> findByAlbumId(int albumId);
}
