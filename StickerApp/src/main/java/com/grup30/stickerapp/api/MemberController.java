package com.grup30.stickerapp.api;

import com.grup30.stickerapp.application.dto.album.responses.StartAlbumCollectionResponse;
import com.grup30.stickerapp.application.dto.forum.responses.ForumMessageResponse;
import com.grup30.stickerapp.application.dto.forum.responses.ForumResponse;
import com.grup30.stickerapp.application.dto.member.requests.SendDirectMessageRequest;
import com.grup30.stickerapp.application.dto.member.requests.UpdateMemberRequest;
import com.grup30.stickerapp.application.dto.member.responses.*;
import com.grup30.stickerapp.application.exceptions.BadRequestException;
import com.grup30.stickerapp.application.services.MemberService;
import com.grup30.stickerapp.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.grup30.stickerapp.utils.Utils.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final SimpMessagingTemplate messagingTemplate;
    public MemberController(MemberService memberService, SimpMessagingTemplate messagingTemplate) {
        this.memberService = memberService;
        this.messagingTemplate = messagingTemplate;
    }

    @PutMapping("/profile")
    public ResponseEntity<UpdateMemberResponse> updateMemberProfile(
            @RequestParam(required = false) Integer memberId,
            @RequestBody UpdateMemberRequest req) {
        return ResponseEntity.ok(memberService.updateMemberProfile(resolveMemberId(memberId), req));
    }

    @GetMapping("/profile")
    public ResponseEntity<MemberResponse> getMemberInfo(@RequestParam(required = false) Integer memberId) {
        return ResponseEntity.ok(memberService.getMemberInfo(resolveMemberId(memberId)));
    }

    @GetMapping("/collections")
    public ResponseEntity<List<SimpleCollectionResponse>> getMemberCollections(
            @RequestParam(required = false) Integer memberId) {
        return ResponseEntity.ok(memberService.getMemberCollections(resolveMemberId(memberId)));
    }

    @PostMapping("/collections/{albumId}")
    public ResponseEntity<StartAlbumCollectionResponse> startAlbumCollection(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer albumId) {
        return ResponseEntity.ok(memberService.startAlbumCollection(resolveMemberId(memberId), albumId));
    }

    @GetMapping("/collections/active")
    public ResponseEntity<List<CollectionResponse>> getActiveCollections(
            @RequestParam(required = false) Integer memberId) {
        return ResponseEntity.ok(memberService.getActiveCollections(resolveMemberId(memberId)));
    }

    @PostMapping("/collection={collectionId}/sticker={stickerId}")
    public ResponseEntity<CollectStickerResponse> addStickerToCollection(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer collectionId,
            @PathVariable Integer stickerId) {
        return ResponseEntity.ok(memberService.addStickerToCollection(resolveMemberId(memberId), collectionId, stickerId));
    }

    @PostMapping("/forum={forumId}/message")
    public ResponseEntity<SendDirectMessageResponse> sendDirectMessageInForum(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer forumId,
            @RequestBody SendDirectMessageRequest req) {
        var sender = resolveMemberId(memberId);
        var message = memberService.sendDirectMessageInForum(sender, forumId, req);
        messagingTemplate.convertAndSend("/topic/direct/" + sender + "/" + req.receiverId(), message);
        messagingTemplate.convertAndSend("/topic/direct/" + req.receiverId() + "/" + sender , message);
        return ResponseEntity.ok(new SendDirectMessageResponse(
                message.id(),
                message.senderId(),
                message.receiverId(),
                message.sentAt(),
                "Message sent successfully"
        ));
    }

    @PostMapping("/forum={forumId}/read-message/{messageId}")
    public ResponseEntity<ReadForumMessageResponse> markForumMessageAsRead(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer forumId,
            @PathVariable Integer messageId) {
        return ResponseEntity.ok(memberService.markForumMessageAsRead(resolveMemberId(memberId), forumId, messageId));
    }

    @GetMapping("/forum={forumId}/unread-forum-messages")
    public ResponseEntity<List<ForumMessageResponse>> getUnreadMessagesFromForum(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer forumId) {
        return ResponseEntity.ok(memberService.getUnreadMessagesFromForum(resolveMemberId(memberId), forumId));
    }

    @GetMapping("/forum={forumId}/unread-direct-messages")
    public ResponseEntity<List<DirectMessageResponse>> getUnreadDirectMessagesFromForum(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer forumId) {
        return ResponseEntity.ok(memberService.getUnreadDirectMessagesFromForum(resolveMemberId(memberId), forumId));
    }

    @PostMapping("/forum={forumId}/read-direct-message/{messageId}")
    public ResponseEntity<ReadDirectMessageResponse> markDirectMessageAsRead(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer forumId,
            @PathVariable Integer messageId) {
        return ResponseEntity.ok(memberService.markDirectMessageAsRead(resolveMemberId(memberId), forumId, messageId));
    }

    @GetMapping("/collection={collectionId}/stickers")
    public ResponseEntity<CheckStickersInCollectionResponse> checkStickersInCollection(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer collectionId) {
        return ResponseEntity.ok(memberService.checkStickersInCollection(resolveMemberId(memberId), collectionId));
    }

    @GetMapping("/forums")
    public ResponseEntity<List<ForumResponse>> getMemberForums(
            @RequestParam(required = false) Integer memberId) {
        return ResponseEntity.ok(memberService.getMemberForums(resolveMemberId(memberId)));
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers(@RequestParam(required = false) String sort) {
        permissionCheck();
        return ResponseEntity.ok(memberService.getAllMembers(sort));
    }

    @GetMapping("/forum={id}/direct-messages")
    public ResponseEntity<List<ChatResponse>> getAllChatsFromForum(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer id) {
        return ResponseEntity.ok(memberService.getAllChatsFromForum(resolveMemberId(memberId), id));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<SimpleMemberResponse>> getAllMembersFromPeriod(
            @RequestParam(required = false) Long days,
            @RequestParam(required = false) Long hours,
            @RequestParam(required = false) Long minutes,
            @RequestParam(required = false) Long seconds,
            @RequestParam(required = false) String sort
    ) {
        permissionCheck();
        if (days == null && hours == null && minutes == null && seconds == null) {
            throw new BadRequestException("At least one period parameter must be provided");
        }
        return ResponseEntity.ok(memberService.getAllMembersWithinAPeriod(getTotalSeconds(days, hours, minutes, seconds), sort));
    }

    @PostMapping("/block/other-member={otherMemberId}")
    public ResponseEntity<BlockMemberResponse> blockMember(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer otherMemberId
    ) {
        return ResponseEntity.ok(memberService.blockMember(resolveMemberId(memberId), otherMemberId));
    }

    @PostMapping("/unblock/other-member={otherMemberId}")
    public ResponseEntity<UnblockMemberResponse> unblockMember(
            @RequestParam(required = false) Integer memberId,
            @PathVariable Integer otherMemberId
    ) {
        return ResponseEntity.ok(memberService.unblockMember(resolveMemberId(memberId), otherMemberId));
    }

    @GetMapping("/active-users")
    public ResponseEntity<List<SimpleMemberResponse>> getMembersByMessageNumDesc() {
        return ResponseEntity.ok(memberService.getMembersByMessageNumDesc());
    }

    /*
    ENDPOINT ------>

        @GetMapping("/potential-exchanges")
        public ResponseEntity<List<ExchangeableMembersResponse>> getPotentialExchanges(
            @RequestParam(required = false) Integer memberId,
            @RequestParam(defaultValue = "3") int limit
        ) {
            Member member = getMemberFromToken();
            if (member.getRole().equals(Role.ADMIN) && memberId != null) {
                return ResponseEntity.ok(memberService.getPotentialExchanges(memberId, limit));
            }
            return ResponseEntity.ok(memberService.getPotentialExchanges(member.getId(), limit));
        }

    RECORDS DTO ------>

        public record ExchangeableMembersResponse(
            int memberId,
            String memberName,
            List<ExchangeableSticker> exchangeableStickers,
            int totalExchangeableStickers
        ) {}
        
        public record ExchangeableSticker(
            int stickerId,
            String stickerName,
            boolean youHaveExtra,
            boolean theyHaveExtra
        ) {}

    MEMBER SERVICE ------>

        public List<ExchangeableMembersResponse> getPotentialExchanges(int memberId, int limit) {
                Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new NotFoundException("Member not found"));
            
                // Get all members except the requesting one
                List<Member> otherMembers = memberRepository.findAll().stream()
                    .filter(m -> !m.getId().equals(memberId))
                    .toList();
            
                return otherMembers.stream()
                    .map(otherMember -> {
                        List<ExchangeableSticker> exchangeableStickers = findExchangeableStickers(member, otherMember);
                        return new ExchangeableMembersResponse(
                            otherMember.getId(),
                            otherMember.getName() + " " + otherMember.getSecondName(),
                            exchangeableStickers,
                            exchangeableStickers.size()
                        );
                    })
                    .sorted(Comparator.comparing(ExchangeableMembersResponse::totalExchangeableStickers).reversed())
                    .limit(limit)
                    .toList();
            }
            
            private List<ExchangeableSticker> findExchangeableStickers(Member member1, Member member2) {
                List<ExchangeableSticker> exchangeableStickers = new ArrayList<>();
            
                // Get collections for both members
                List<Collection> member1Collections = member1.getCollections();
                List<Collection> member2Collections = member2.getCollections();
            
                // Compare each collection
                for (Collection col1 : member1Collections) {
                    for (Collection col2 : member2Collections) {
                        if (col1.getAlbum().getId().equals(col2.getAlbum().getId())) {
                            // Get stickers for this album
                            List<Sticker> stickers1 = col1.getStickersCollected();
                            List<Sticker> stickers2 = col2.getStickersCollected();
            
                            // Find duplicates and missing stickers
                            Map<Sticker, Long> stickersCount1 = stickers1.stream()
                                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
                            Map<Sticker, Long> stickersCount2 = stickers2.stream()
                                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
            
                            // Check each sticker in the album
                            col1.getAlbum().getSections().stream()
                                .flatMap(section -> section.getStickers().stream())
                                .forEach(sticker -> {
                                    boolean member1HasExtra = stickersCount1.getOrDefault(sticker, 0L) > 1;
                                    boolean member2HasExtra = stickersCount2.getOrDefault(sticker, 0L) > 1;
                                    boolean member1Missing = !stickersCount1.containsKey(sticker);
                                    boolean member2Missing = !stickersCount2.containsKey(sticker);
            
                                    if ((member1HasExtra && member2Missing) || (member2HasExtra && member1Missing)) {
                                        exchangeableStickers.add(new ExchangeableSticker(
                                            sticker.getId(),
                                            sticker.getName(),
                                            member1HasExtra,
                                            member2HasExtra
                                        ));
                                    }
                                });
                        }
                    }
                }
            
                return exchangeableStickers;
            }
     */
}
