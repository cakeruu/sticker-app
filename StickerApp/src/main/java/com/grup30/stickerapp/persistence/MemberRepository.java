package com.grup30.stickerapp.persistence;

import com.grup30.stickerapp.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    List<Member> findByDateOfRegistrationBetweenOrderBySecondNameAsc(LocalDateTime startDate, LocalDateTime endDate);
    List<Member> findByDateOfRegistrationBetweenOrderBySecondNameDesc(LocalDateTime startDate, LocalDateTime endDate);
    List<Member> findAllByOrderBySecondNameAsc();
    List<Member> findAllByOrderBySecondNameDesc();
    @Query("SELECT m FROM Member m JOIN m.forums f WHERE f.id = :forumId")
    List<Member> findMembersByForumId(@Param("forumId") Integer forumId);
    Optional<Member> findByEmail(String email);
    // TODO: We may have to change return type to List<Object[]>
    @Query("SELECT m, (COUNT(dm) + COUNT(fm)) AS messageCount " +
            "FROM Member m " +
            "JOIN DirectMessage dm ON dm.sender = m " +
            "JOIN ForumMessage fm ON fm.sender = m " +
            "GROUP BY m " +
            "ORDER BY COUNT(dm) + COUNT(fm) DESC")
    List<Member> findAllByMessageNumDesc();
}
