package com.grup30.stickerapp.application.dto.album.requests;

import com.grup30.stickerapp.application.exceptions.BadRequestException;
import com.grup30.stickerapp.domain.*;

import java.time.LocalDateTime;
import java.util.List;

public record CreateAlbumRequest(
        String name,
        LocalDateTime beginningDate,
        LocalDateTime endingDate,
        String editor,
        boolean isPublic,
        List<Section> sections
) {
    public Album toAlbum(Member owner, Forum forum) {
        List<Section> sections = sections();
        if (sections == null || sections.isEmpty()) {
            throw new BadRequestException("Sections must not be empty");
        }
        sections.forEach(section -> {
            if (section.getStickers() == null || section.getStickers().isEmpty()) {
                throw new BadRequestException("Section " + section.getName() + " must have at least one sticker");
            }
        });
        return new Album(
                name(),
                beginningDate(),
                endingDate(),
                editor(),
                owner,
                forum,
                isPublic(),
                sections
        );
    }
}
