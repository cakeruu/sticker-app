package com.grup30.stickerapp.persistence;

import com.grup30.stickerapp.domain.Forum;
import com.grup30.stickerapp.domain.ForumMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ForumMessageRepository extends JpaRepository<ForumMessage, Integer> {
    List<ForumMessage> findByForumIdAndSentAtBetweenOrderBySentAtAsc(Integer forumId, LocalDateTime startDate, LocalDateTime endDate);

    List<ForumMessage> findByForum(Forum forum);
}

