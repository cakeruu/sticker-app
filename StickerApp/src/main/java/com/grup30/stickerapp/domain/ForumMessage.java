package com.grup30.stickerapp.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Message")
public class ForumMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "forumId")
    private Forum forum;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member sender;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private LocalDateTime sentAt;

    @ElementCollection
    @CollectionTable(name = "forum_message_readers", joinColumns = @JoinColumn(name = "message_id"))
    @Column(name = "member_id")
    private Set<Integer> readers = new HashSet<>();

    public ForumMessage() {
    }

    public ForumMessage(Forum forum, Member sender, String content, LocalDateTime sentAt) {
        this.forum = forum;
        this.sender = sender;
        this.content = content;
        this.sentAt = sentAt;
        readers.add(sender.getId());
    }

    public Integer getId() {
        return id;
    }

    public Forum getForum() {
        return forum;
    }

    public Member getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public Set<Integer> getReaders() {
        return readers;
    }

    public void markAsReadBy(Integer memberId) {
        readers.add(memberId);
    }

    public boolean isReadBy(Integer memberId) {
        return readers.contains(memberId);
    }

    public boolean isInForum(Forum forum) {
        return this.forum.equals(forum);
    }
}
