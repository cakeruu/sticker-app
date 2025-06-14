package com.grup30.stickerapp.application.dto.album.responses;

import com.grup30.stickerapp.application.dto.member.responses.SimpleMemberResponse;
import com.grup30.stickerapp.domain.Forum;
import com.grup30.stickerapp.domain.Section;

import java.util.List;

public record GetAlbumWithDetailsResponse(
        Integer id,
        String name,
        String beginningDate,
        String endingDate,
        String editor,
        SimpleMemberResponse owner,
        Forum forum,
        boolean isPublic,
        List<Section> sections
) { }
