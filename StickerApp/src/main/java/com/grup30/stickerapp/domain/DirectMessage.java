package com.grup30.stickerapp.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "DirectMessage")
public class DirectMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String content;
    @ManyToOne
    @JoinColumn(name = "forumId")
    private Forum forum;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Member sender;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Member receiver;
    @Column(nullable = false)
    private boolean isRead;
    @Column(nullable = false)
    private LocalDateTime sentAt;
    @Column(nullable = false)
    private boolean isOnetime;

    public DirectMessage() {
    }

    public DirectMessage(String content, Member sender, Member receiver, LocalDateTime sentAt, Forum forum, boolean isOnetime) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.forum = forum;
        this.isRead = false;
        this.sentAt = sentAt;
        this.isOnetime = isOnetime;
    }

    public Integer getId() {
        return id;
    }
    public String getContent() {
        return content;
    }
    public Member getSender() {
        return sender;
    }
    public Member getReceiver() {
        return receiver;
    }
    public LocalDateTime getSentAt() {
        return sentAt;
    }
    public boolean isRead() {
        return isRead;
    }
    public boolean isOnetime() {
        return isOnetime;
    }
    public void markAsRead() {
        this.isRead = true;
    }
    public boolean isInForum(int forumId) {
        return forum.getId() == forumId;
    }
}

