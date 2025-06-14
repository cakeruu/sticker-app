package com.grup30.stickerapp.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Collection")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "collection_stickers",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "sticker_id")
    )
    private List<Sticker> stickersCollected;

    public Collection() {}

    public Collection(Member member, Album album) {
        this.member = member;
        this.album = album;
        this.stickersCollected = List.of();
    }

    public Integer getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Album getAlbum() {
        return album;
    }

    public List<Sticker> getStickersCollected() {
        return stickersCollected;
    }

    public boolean isActiveAndPublic() {
        return album.isActive() && album.isPublic();
    }

    public void addSticker(Sticker sticker) {
        stickersCollected.add(sticker);
    }

    public void removeSticker(Sticker sticker) {
        stickersCollected.remove(sticker);
    }
}
