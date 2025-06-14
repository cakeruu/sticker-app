package com.grup30.stickerapp.api;

import com.grup30.stickerapp.application.services.StickerService;
import com.grup30.stickerapp.domain.Sticker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/stickers")
public class StickerController {
    private final StickerService stickerService;

    public StickerController(StickerService stickerService) {
        this.stickerService = stickerService;
    }

    @GetMapping("/{number}/album={albumId}")
    public ResponseEntity<List<Sticker>> getStickersByNumberAndAlbumId(@PathVariable int number, @PathVariable int albumId) {
        return ResponseEntity.ok(stickerService.getStickersByNumberAndAlbumId(albumId, number));
    }
}
