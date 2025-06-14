package com.grup30.stickerapp.persistence;

import com.grup30.stickerapp.domain.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Integer> {
    @Query("SELECT f " +
            "FROM Forum f " +
            "JOIN ForumMessage fm ON fm.forum = f " +
            "GROUP BY f " +
            "ORDER BY COUNT(fm) DESC")
    List<Forum> findAllByForumMessageNumDesc();
}
