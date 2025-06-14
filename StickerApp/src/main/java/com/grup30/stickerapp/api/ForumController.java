package com.grup30.stickerapp.api;

import com.grup30.stickerapp.application.dto.forum.requests.SendForumMessageRequest;
import com.grup30.stickerapp.application.dto.forum.responses.*;
import com.grup30.stickerapp.application.dto.member.responses.SimpleMemberResponse;
import com.grup30.stickerapp.application.services.ForumService;
import com.grup30.stickerapp.domain.Forum;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.grup30.stickerapp.utils.Utils.*;

@RestController
@RequestMapping("/api/forums")
public class ForumController {
    private final ForumService forumService;

    private final SimpMessagingTemplate messagingTemplate;

    public ForumController(ForumService memberService, SimpMessagingTemplate messagingTemplate) {
        this.forumService = memberService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/{id}/register-member")
    public ResponseEntity<RegisterMemberResponse> registerMember(@PathVariable Integer id, @RequestParam(required = false) Integer memberId) {
        return ResponseEntity.ok(forumService.registerMember(resolveMemberId(memberId), id));
    }

    @PostMapping("/{id}/unregister-member")
    public ResponseEntity<UnregisterMemberResponse> unregisterMember(@PathVariable Integer id, @PathVariable(required = false) Integer memberId) {
        return ResponseEntity.ok(forumService.unregisterMember(resolveMemberId(memberId), id));
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<ForumMessageResponse>> getAllMessagesFromForumWithPeriod(
            @RequestParam(required = false) Long days,
            @RequestParam(required = false) Long hours,
            @RequestParam(required = false) Long minutes,
            @RequestParam(required = false) Long seconds,
            @PathVariable Integer id
    )
    {
        return ResponseEntity.ok(forumService.getAllMessagesFromForumWithinAPeriod(id, getTotalSeconds(days, hours, minutes, seconds)));
    }

    @PostMapping("/messages")
    public ResponseEntity<SendForumMessageResponse> sendForumMessage(@Valid @RequestBody SendForumMessageRequest req) {
        var message = forumService.sendForumMessage(req, getMemberFromToken().getId());
        messagingTemplate.convertAndSend("/topic/forum/" + req.forumId(), message);
        return ResponseEntity.ok(new SendForumMessageResponse(
              message.id(),
              "Message sent to the forum successfully"
        ));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<SimpleMemberResponse>> getMembersInForum(@PathVariable Integer id) {
        return ResponseEntity.ok(forumService.getMembersInForum(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForumResponse> getForum(@PathVariable Integer id) {
        return ResponseEntity.ok(forumService.getForum(id));
    }
    @GetMapping("/top-messages")
    public ResponseEntity<List<Forum>> getForumByForumMessageNumDesc() {
       return ResponseEntity.ok(forumService.getForumByForumMessageNumDesc());
    }
}