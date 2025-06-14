package com.grup30.stickerapp.api;

import com.grup30.stickerapp.application.dto.member.requests.CreateMemberRequest;
import com.grup30.stickerapp.application.dto.member.requests.LoginRequest;
import com.grup30.stickerapp.application.dto.member.responses.CreateMemberResponse;
import com.grup30.stickerapp.application.dto.member.responses.LoginResponse;
import com.grup30.stickerapp.application.services.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<CreateMemberResponse> createMember(@RequestBody CreateMemberRequest req) {
        return ResponseEntity.ok(memberService.createMember(req));
    }
    @PostMapping("members/login")
    public ResponseEntity<LoginResponse> memberLogin(@RequestBody LoginRequest req) {
        return memberService.memberLogin(req);
    }

    //api/auth/verify
    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken() {
        return ResponseEntity.ok().build();
    }
}
