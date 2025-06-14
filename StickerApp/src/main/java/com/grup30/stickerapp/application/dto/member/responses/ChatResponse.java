package com.grup30.stickerapp.application.dto.member.responses;

import java.util.List;

public record ChatResponse (
    Integer firstMemberId,
    Integer secondMemberId,
    String firstMemberName,
    String secondMemberFullName,
    List<DirectMessageResponse> messages
) {
}
