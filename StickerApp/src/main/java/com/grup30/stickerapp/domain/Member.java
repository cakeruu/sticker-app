package com.grup30.stickerapp.domain;

import com.grup30.stickerapp.application.dto.member.requests.UpdateMemberRequest;
import com.grup30.stickerapp.application.exceptions.BadRequestException;
import com.grup30.stickerapp.domain.enums.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String secondName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private LocalDateTime dateOfBirth;
    @Column(nullable = false)
    private LocalDateTime dateOfRegistration;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "member_forum",
            joinColumns = @JoinColumn(name = "memberId"),
            inverseJoinColumns = @JoinColumn(name = "forumId")
    )
    private List<Forum> forums;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Collection> collections;

    @ManyToMany
    @JoinTable(
            name = "banned_members",
            joinColumns = @JoinColumn(name = "banned_by"),
            inverseJoinColumns = @JoinColumn(name = "banned_member")
    )
    private List<Member> bannedMembers;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member() {
    }

    public Member(String password, String name, String secondName, String email,
                  LocalDateTime dateOfBirth, LocalDateTime dateOfRegistration, Role role) {
        this.password = password;
        this.name = name;
        this.secondName = secondName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.dateOfRegistration = dateOfRegistration;
        this.forums = List.of();
        this.collections = List.of();
        this.bannedMembers = List.of();
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDateTime getDateOfRegistration() {
        return dateOfRegistration;
    }

    public List<Forum> getForums() {
        return forums;
    }

    public void registerToForum(Forum forum) {
        forums.add(forum);
    }

    public void unregisterFromForum(Forum forum) {
        forums.remove(forum);
    }

    public void updateProfile(UpdateMemberRequest req, String hashedPassword) {
        this.password = hashedPassword;
        this.name = req.name();
        this.secondName = req.secondName();
        this.email = req.email();
        this.dateOfBirth = req.dateOfBirth();
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void saveCollection(Collection collection) {
        collections.add(collection);
    }

    public boolean isMemberPartOfForum(Forum forum) {
        return forums.contains(forum);
    }

    public Role getRole() {
        return role;
    }

    public void blockMember(Member member) {
        if (!bannedMembers.contains(member)) {
            bannedMembers.add(member);
            return;
        }
        throw new BadRequestException("Member is already blocked");
    }

    public void unblockMember(Member member) {
        if (bannedMembers.contains(member)) {
            bannedMembers.remove(member);
            return;
        }
        throw new BadRequestException("Member is not blocked");
    }

    public void startAlbumCollection (Album album) {
        Collection collection = new Collection(this, album);
        collections.add(collection);
    }

    public boolean isMemberBlocked(Member member) {
        return bannedMembers.contains(member);
    }
}
