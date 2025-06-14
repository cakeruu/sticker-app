package com.grup30.stickerapp.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Sticker")
public class Sticker {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column (nullable = false)
        private Integer number;
        @Column(nullable = false)
        private String name;
        @Column(nullable = false)
        private String description;
        private String image;

        public Sticker() {}

        public Sticker(String name, String description, String image, Integer number) {
                this.name = name;
                this.description = description;
                this.image = image;
                this.number = number;
        }

        public Integer getId() {
                return id;
        }
        public String getName() {
                return name;
        }
        public String getDescription() {
                return description;
        }
        public String getImage() {
                return image;
        }
        public Integer getNumber() {
                return number;
        }
}
