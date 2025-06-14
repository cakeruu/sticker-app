package com.grup30.stickerapp.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Section")
public class Section {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        @Column(nullable = false)
        private String name;

        // Cambio de ManyToOne a OneToMany (Borrado el ManyToOne en Sticker)
        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "section_id")
        private List<Sticker> stickers;

        public Section() {}

        public Section(String name, List<Sticker> stickers) {
                this.name = name;
                this.stickers = stickers;
        }

        public void saveSticker(Sticker sticker) {
                stickers.add(sticker);
        }

        public Integer getId() {
                return id;
        }
        public String getName() {
                return name;
        }
        public List<Sticker> getStickers() {
                return stickers;
        }
}
