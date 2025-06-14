package com.grup30.stickerapp.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime beginningDate;

    @Column(nullable = false)
    private LocalDateTime endingDate;

    @Column(nullable = false)
    private String editor;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Member owner;

    @OneToOne
    @JoinColumn(name = "forumId")
    private Forum forum;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean isPublic;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "album_id")
    private List<Section> sections;

    public Album() {
    }

    public Album(
            String name,
            LocalDateTime beginningDate,
            LocalDateTime endingDate,
            String editor,
            Member owner,
            Forum forum,
            boolean isPublic,
            List<Section> sections
    ) {
        this.name = name;
        this.beginningDate = beginningDate;
        this.endingDate = endingDate;
        this.editor = editor;
        this.owner = owner;
        this.forum = forum;
        this.isPublic = isPublic;
        this.sections = sections;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getBeginningDate() {
        return beginningDate;
    }

    public LocalDateTime getEndingDate() {
        return endingDate;
    }

    public String getEditor() {
        return editor;
    }

    public Member getOwner() {
        return owner;
    }

    public Forum getForum() {
        return forum;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setVisibility(boolean visibility) {
        isPublic = visibility;
    }

    public boolean isActive() {
        return LocalDateTime.now().isAfter(beginningDate) && LocalDateTime.now().isBefore(endingDate);
    }

    public boolean isPublicAndActive() {
        return isPublic && isActive();
    }
}