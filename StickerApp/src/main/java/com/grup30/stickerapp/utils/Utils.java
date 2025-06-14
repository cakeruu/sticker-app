package com.grup30.stickerapp.utils;

import com.grup30.stickerapp.application.dto.album.responses.AlbumResponse;
import com.grup30.stickerapp.application.dto.album.responses.GetAlbumWithDetailsResponse;
import com.grup30.stickerapp.application.dto.member.responses.CollectionResponse;
import com.grup30.stickerapp.application.dto.member.responses.SectionStickerGroupResponse;
import com.grup30.stickerapp.application.dto.member.responses.SimpleMemberResponse;
import com.grup30.stickerapp.application.exceptions.ForbiddenException;
import com.grup30.stickerapp.application.exceptions.NotFoundException;
import com.grup30.stickerapp.config.security.SecurityUser;
import com.grup30.stickerapp.domain.*;
import com.grup30.stickerapp.domain.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utils {
    /**
     * Converts the provided time period parameters (days, hours, minutes, seconds) into total seconds.
     * If a parameter is not provided, it is considered as zero.
     *
     * @param days    the number of days provided
     * @param hours   the number of hours provided
     * @param minutes the number of minutes provided
     * @param seconds the number of seconds provided
     * @return the total time period in seconds provided
     */
    public static long getTotalSeconds(Long days, Long hours, Long minutes, Long seconds) {
        long totalSeconds = 0;
        if (days != null)
            totalSeconds += days * 86400;
        if (hours != null)
            totalSeconds += hours * 3600;
        if (minutes != null)
            totalSeconds += minutes * 60;
        if (seconds != null)
            totalSeconds += seconds;
        return totalSeconds;
    }

    /**
     * Generates a map of extra claims for the given security user.
     * The claims include the member's ID, full name, email, and roles.
     *
     * @param user the security user for whom to generate extra claims
     * @return a map containing the extra claims
     * @throws IllegalArgumentException if the user or member is null
     */
    public static Map<String, Object> generateExtraClaims(SecurityUser user) {
        if (user == null || user.member() == null) {
            throw new IllegalArgumentException("User or member cannot be null");
        }
        Member member = user.member();
        return Map.of(
                "id", member.getId().toString(),
                "user_full_name", member.getName() + " " + member.getSecondName(),
                "user_name", member.getName(),
                "email", member.getEmail(),
                "roles", getUserRoles(user));
    }

    public static String[] getUserRoles(SecurityUser user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .distinct()  // Remove duplicate roles
                .toArray(String[]::new);
    }


    /**
     * Retrieves the member associated with the current authentication token.
     *
     * @return the member associated with the current authentication token
     */
    public static Member getMemberFromToken() {
        var user = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.member();
    }


    /**
     * Resolves the member ID based on the provided user ID and the role of the member from the token.
     * If the member has an ADMIN role and a user ID is provided, it returns the provided user ID.
     * Otherwise, it throws a ForbiddenException indicating that the action is not allowed.
     *
     * @param userId the user ID to be resolved (optional)
     * @return the resolved member ID
     * @throws ForbiddenException if the member does not have an ADMIN role or the user ID is not provided
     */
    public static Integer resolveMemberId(Integer userId) {
        Member member = getMemberFromToken();

        if (userId == null) return member.getId(); // If the user ID is not provided, return the member ID

        if (member.getRole().equals(Role.ADMIN)) {
            return userId;
        }
        throw new ForbiddenException("You are not allowed to perform this action");
    }

    public static void permissionCheck() {
        Member member = getMemberFromToken();
        if (!member.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenException("You are not allowed to perform this action");
        }
    }

    public static List<CollectionResponse> parseCollections(List<Collection> collections) {
        return collections.stream()
                .map(Utils::parseCollection)
                .toList();
    }

    public static CollectionResponse parseCollection(Collection collection) {
        Album album = collection.getAlbum();

        Map<Integer, List<Sticker>> stickersBySection = collection.getStickersCollected().stream()
                .collect(Collectors.groupingBy(sticker -> findSectionIdForSticker(sticker, album)));

        List<SectionStickerGroupResponse> stickers = stickersBySection.entrySet().stream()
                .map(entry -> {
                    int sectionId = entry.getKey();
                    String sectionName = findSectionNameById(sectionId, album);
                    return new SectionStickerGroupResponse(
                            sectionId,
                            sectionName,
                            entry.getValue()
                    );
                })
                .toList();

        return new CollectionResponse(
                collection.getId(),
                collection.getMember().getId(),
                album.getId(),
                album.getName(),
                stickers
        );
    }
    private static int findSectionIdForSticker(Sticker sticker, Album album) {
        return album.getSections().stream()
                .filter(section -> section.getStickers().contains(sticker))
                .map(Section::getId) // Assuming `Section` has a `getId()` method
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Section not found for the given sticker"));
    }

    private static String findSectionNameById(int sectionId, Album album) {
        return album.getSections().stream()
                .filter(section -> section.getId() == sectionId)
                .map(Section::getName)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Section not found for ID: " + sectionId));
    }


    public static <T> T parseAlbum(Album album, Function<Album, T> mapper) {
        return mapper.apply(album);
    }

    public static <T> List<T> parseAlbums(List<Album> albums, Function<Album, T> mapper) {
        return albums.stream()
                .map(mapper)
                .toList();
    }

    public static AlbumResponse mapToAlbumResponse(Album album) {
        Member owner = album.getOwner();
        String ownerFullName = owner.getName() + " " + owner.getSecondName();
        return new AlbumResponse(
                album.getId(),
                album.getName(),
                album.getBeginningDate().toString(),
                album.getEndingDate().toString(),
                album.getEditor(),
                owner.getId(),
                ownerFullName,
                album.getForum().getId(),
                album.isPublic()
        );
    }

    public static GetAlbumWithDetailsResponse mapToGetAlbumWithDetailsResponse(Album album) {
        Member owner = album.getOwner();
        String ownerFullName = owner.getName() + " " + owner.getSecondName();
        return new GetAlbumWithDetailsResponse(
                album.getId(),
                album.getName(),
                album.getBeginningDate().toString(),
                album.getEndingDate().toString(),
                album.getEditor(),
                new SimpleMemberResponse(
                        owner.getId(),
                        ownerFullName,
                        owner.getEmail(),
                        owner.getDateOfBirth(),
                        owner.getDateOfRegistration()
                ),
                album.getForum(),
                album.isPublic(),
                album.getSections()
        );
    }
}
