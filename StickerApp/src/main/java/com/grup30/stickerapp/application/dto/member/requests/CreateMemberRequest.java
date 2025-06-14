package com.grup30.stickerapp.application.dto.member.requests;

import com.grup30.stickerapp.domain.Member;
import com.grup30.stickerapp.domain.enums.Role;

import java.time.LocalDateTime;

public record CreateMemberRequest(
        String password,
        String name,
        String secondName,
        String email,
        LocalDateTime dateOfBirth,
        Role role
)
{

    public Member toMember(String passwordEncoded)
    {
        return new Member(
                passwordEncoded,
                this.name(),
                this.secondName(),
                this.email(),
                this.dateOfBirth(),
                LocalDateTime.now(),
                this.role()

        );
    }
}
