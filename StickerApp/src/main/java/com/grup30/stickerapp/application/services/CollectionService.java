package com.grup30.stickerapp.application.services;

import com.grup30.stickerapp.application.dto.member.responses.CollectionResponse;
import com.grup30.stickerapp.application.dto.member.responses.SimpleMemberResponse;
import com.grup30.stickerapp.application.exceptions.ForbiddenException;
import com.grup30.stickerapp.application.exceptions.NotFoundException;
import com.grup30.stickerapp.domain.Collection;
import com.grup30.stickerapp.domain.Member;
import com.grup30.stickerapp.persistence.CollectionRepository;
import com.grup30.stickerapp.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.grup30.stickerapp.utils.Utils.parseCollection;

@Service
public class CollectionService {
    private final CollectionRepository collectionRepository;
    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public List<SimpleMemberResponse> getCollectorsByAlbumId(int albumId) {
        List<Collection> collections = collectionRepository.findByAlbumId(albumId).orElseThrow(() -> new NotFoundException("Collections not found"));
        return collections.stream()
                .map(collection -> {
                    Member member = collection.getMember();
                    String fullName = member.getName() + " " + member.getSecondName();
                    return new SimpleMemberResponse(
                            member.getId(),
                            fullName,
                            member.getEmail(),
                            member.getDateOfBirth(),
                            member.getDateOfRegistration()
                    );
                })
                .toList();
    }


    public CollectionResponse getCollectionById(int id) {
        Collection collection = collectionRepository.findById(id).orElseThrow(() -> new NotFoundException("Collection not found"));
        var member = Utils.getMemberFromToken();
        if (!collection.getMember().getId().equals(member.getId())) {
            throw new ForbiddenException("You cannot access this collection");
        }
        return parseCollection(collection);
    }
}
