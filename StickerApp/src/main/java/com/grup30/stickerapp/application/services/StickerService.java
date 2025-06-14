package com.grup30.stickerapp.application.services;

import com.grup30.stickerapp.application.exceptions.NotFoundException;
import com.grup30.stickerapp.domain.Album;
import com.grup30.stickerapp.domain.Sticker;
import com.grup30.stickerapp.persistence.AlbumRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StickerService {
    private final AlbumRepository albumRepository;

    public StickerService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public List<Sticker> getStickersByNumberAndAlbumId(int id, int number) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Album not found"));

        return album.getSections().stream()
                .flatMap(section -> section.getStickers().stream())
                .filter(sticker -> sticker.getNumber() == number)
                .collect(Collectors.toList());
    }
}
