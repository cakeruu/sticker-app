package com.grup30.stickerapp.application.services;

import com.grup30.stickerapp.application.dto.album.requests.CreateAlbumRequest;
import com.grup30.stickerapp.application.dto.album.responses.ChangeAlbumVisibilityResponse;
import com.grup30.stickerapp.application.dto.album.responses.CreateAlbumResponse;
import com.grup30.stickerapp.application.dto.album.responses.AlbumResponse;
import com.grup30.stickerapp.application.dto.album.responses.GetAlbumWithDetailsResponse;
import com.grup30.stickerapp.application.dto.member.responses.SimpleMemberResponse;
import com.grup30.stickerapp.application.exceptions.NotFoundException;
import com.grup30.stickerapp.domain.*;
import com.grup30.stickerapp.persistence.*;
import com.grup30.stickerapp.utils.Utils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import static com.grup30.stickerapp.utils.Utils.parseAlbum;
import static com.grup30.stickerapp.utils.Utils.parseAlbums;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final MemberRepository memberRepository;
    private final ForumRepository forumRepository;

    public AlbumService(AlbumRepository albumRepository, ForumRepository forumRepository, MemberRepository memberRepository) {
        this.albumRepository = albumRepository;
        this.memberRepository = memberRepository;
        this.forumRepository = forumRepository;
    }

    public List<AlbumResponse> getAlbums() {
        List<Album> albums = albumRepository.findAll();
        return parseAlbums(albums, Utils::mapToAlbumResponse);
    }

    public List<AlbumResponse> getAvailableAlbums() {
        List<Album> albums = albumRepository.findAvailableAlbums(LocalDateTime.now());
        return parseAlbums(albums, Utils::mapToAlbumResponse);
    }

    public AlbumResponse getAlbum(int id) {
        Album album = albumRepository.findById(id).orElseThrow(() -> new NotFoundException("Album not found"));
        return parseAlbum(album, Utils::mapToAlbumResponse);
    }

    @Transactional
    public CreateAlbumResponse createAlbum(CreateAlbumRequest req, int ownerId) {
        Member owner = memberRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("Member not found"));
        Forum forum = createForum();

        Album savedAlbum = albumRepository.save(req.toAlbum(owner, forum));

        owner.startAlbumCollection(savedAlbum);
        owner.registerToForum(forum);

        return new CreateAlbumResponse(
                savedAlbum.getId(),
                "Album and forum created successfully"
        );
    }

    public ChangeAlbumVisibilityResponse changeAlbumVisibility(int id, boolean visibility) {
        Album album = albumRepository.findById(id).orElseThrow(() -> new NotFoundException("Album not found"));
        album.setVisibility(visibility);
        Album savedAlbum = albumRepository.save(album);
        return new ChangeAlbumVisibilityResponse(
                savedAlbum.getId(),
                savedAlbum.isPublic(),
                "Album visibility changed successfully"
        );
    }

    public GetAlbumWithDetailsResponse getAlbumWithDetails(int id) {
        Album album = albumRepository.findById(id).orElseThrow(() -> new NotFoundException("Album not found"));
        return parseAlbum(album, Utils::mapToGetAlbumWithDetailsResponse);
    }

    public List<AlbumResponse> getAlbumsByMember(Integer userId) {
        Member member = memberRepository.findById(userId).orElseThrow(() -> new NotFoundException("Member not found"));
        List<Album> albums = albumRepository.findByOwner(member);
        return parseAlbums(albums, Utils::mapToAlbumResponse);
    }

    // private functions
    private Forum createForum() {
        return forumRepository.save(new Forum(null,
                LocalDateTime.now()));
    }
}
