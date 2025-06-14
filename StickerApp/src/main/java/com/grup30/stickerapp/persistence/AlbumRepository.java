package com.grup30.stickerapp.persistence;

import com.grup30.stickerapp.domain.Album;
import com.grup30.stickerapp.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {
    @Query("SELECT a FROM Album a WHERE a.beginningDate <= :now AND a.endingDate >= :now AND a.isPublic = true")
    List<Album> findAvailableAlbums(@Param("now") LocalDateTime now);

    Optional<Album> findByForumId(Integer id);

    List<Album> findByOwner(Member member);
}
