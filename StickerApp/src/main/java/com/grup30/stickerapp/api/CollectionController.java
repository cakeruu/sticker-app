package com.grup30.stickerapp.api;

import com.grup30.stickerapp.application.dto.member.responses.CollectionResponse;
import com.grup30.stickerapp.application.dto.member.responses.SimpleMemberResponse;
import com.grup30.stickerapp.application.services.CollectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
public class CollectionController {
    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @GetMapping("/album={albumId}/collectors")
    public ResponseEntity<List<SimpleMemberResponse>> getCollectorsByAlbumId(@PathVariable int albumId) {
        return ResponseEntity.ok(collectionService.getCollectorsByAlbumId(albumId));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CollectionResponse> getCollectionById(@PathVariable int id) {
        return ResponseEntity.ok(collectionService.getCollectionById(id));
    }
}
