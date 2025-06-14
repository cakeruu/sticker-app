package com.grup30.stickerapp.api;

import com.grup30.stickerapp.application.dto.album.requests.CreateAlbumRequest;
import com.grup30.stickerapp.application.dto.album.responses.ChangeAlbumVisibilityResponse;
import com.grup30.stickerapp.application.dto.album.responses.CreateAlbumResponse;
import com.grup30.stickerapp.application.dto.album.responses.AlbumResponse;
import com.grup30.stickerapp.application.dto.album.responses.GetAlbumWithDetailsResponse;
import com.grup30.stickerapp.application.services.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.grup30.stickerapp.utils.Utils.resolveMemberId;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public ResponseEntity<List<AlbumResponse>> getAlbums() {
        return ResponseEntity.ok(albumService.getAlbums());
    }

    @GetMapping("/member")
    public ResponseEntity<List<AlbumResponse>> getAlbumsByMember(@RequestParam(required = false) Integer memberId) {
        return ResponseEntity.ok(albumService.getAlbumsByMember(resolveMemberId(memberId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponse> getAlbum(@PathVariable int id) {
        return ResponseEntity.ok(albumService.getAlbum(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<AlbumResponse>> getAvailableAlbums() {
        return ResponseEntity.ok(albumService.getAvailableAlbums());
    }

    @PostMapping()
    public ResponseEntity<CreateAlbumResponse> createAlbum(@RequestBody CreateAlbumRequest createAlbumRequest, @RequestParam(required = false) Integer memberId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(albumService.createAlbum(createAlbumRequest, resolveMemberId(memberId)));
    }

    @PutMapping("/{id}/isPublic={visibility}")
    public ResponseEntity<ChangeAlbumVisibilityResponse> changeAlbumVisibility(@PathVariable int id, @PathVariable boolean visibility) {
        return ResponseEntity.ok(albumService.changeAlbumVisibility(id, visibility));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<GetAlbumWithDetailsResponse> getAlbumWithDetails(@PathVariable int id) {
        return ResponseEntity.ok(albumService.getAlbumWithDetails(id));
    }
}
