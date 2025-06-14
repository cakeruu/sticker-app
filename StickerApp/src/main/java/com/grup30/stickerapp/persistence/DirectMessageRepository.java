package com.grup30.stickerapp.persistence;

import com.grup30.stickerapp.domain.DirectMessage;
import com.grup30.stickerapp.domain.Forum;
import com.grup30.stickerapp.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessage, Integer> {
    List<DirectMessage> findByReceiverAndForumAndIsReadIsFalse(Member receiver, Forum forum);
    @Query("SELECT dm FROM DirectMessage dm " +
            "WHERE dm.forum.id = :forumId " +
            "AND ((dm.sender.id = :firstMemberId AND dm.receiver.id = :secondMemberId) " +
            "OR (dm.sender.id = :secondMemberId AND dm.receiver.id = :firstMemberId)) " +
            "ORDER BY dm.sentAt ASC")
    List<DirectMessage> findDirectMessagesBetweenMembers(
            @Param("firstMemberId") Integer firstMemberId,
            @Param("secondMemberId") Integer secondMemberId,
            @Param("forumId") Integer forumId
    );
}