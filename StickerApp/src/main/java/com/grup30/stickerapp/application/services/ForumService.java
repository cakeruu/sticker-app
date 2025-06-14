package com.grup30.stickerapp.application.services;

import com.grup30.stickerapp.application.dto.forum.requests.SendForumMessageRequest;
import com.grup30.stickerapp.application.dto.forum.responses.*;
import com.grup30.stickerapp.application.dto.member.responses.SimpleMemberResponse;
import com.grup30.stickerapp.application.exceptions.BadRequestException;
import com.grup30.stickerapp.application.exceptions.NotFoundException;
import com.grup30.stickerapp.config.Config;
import com.grup30.stickerapp.config.ConfigService;
import com.grup30.stickerapp.domain.Album;
import com.grup30.stickerapp.domain.Forum;
import com.grup30.stickerapp.domain.Member;
import com.grup30.stickerapp.domain.ForumMessage;
import com.grup30.stickerapp.persistence.AlbumRepository;
import com.grup30.stickerapp.persistence.ForumRepository;
import com.grup30.stickerapp.persistence.MemberRepository;
import com.grup30.stickerapp.persistence.ForumMessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForumService {
    private final ForumRepository forumRepository;
    private final MemberRepository memberRepository;
    private final ForumMessageRepository forumMessageRepository;
    private final AlbumRepository albumRepository;
    private final Config config;

    public ForumService(
            ForumRepository forumRepository,
            MemberRepository memberRepository,
            ForumMessageRepository forumMessageRepository, AlbumRepository albumRepository,
            ConfigService configService
    )
    {
        this.forumRepository = forumRepository;
        this.memberRepository = memberRepository;
        this.forumMessageRepository = forumMessageRepository;
        this.albumRepository = albumRepository;
        this.config = configService.getAppConfig().defaultConfig();
    }

    @Transactional
    public RegisterMemberResponse registerMember(int memberId, int forumId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("Member not found"));
        Forum forum = forumRepository.findById(forumId).orElseThrow(() -> new NotFoundException("Forum not found"));
        Album album = albumRepository.findByForumId(forumId).orElseThrow(() -> new NotFoundException("Album not found"));

        if (member.isMemberPartOfForum(forum)) throw new BadRequestException("Member is already registered in forum");

        member.startAlbumCollection(album); // Start album collection when member registers to forum

        member.registerToForum(forum);
        return new RegisterMemberResponse(
                member.getId(),
                forum.getId(),
                "Member registered in forum successfully"
        );
    }

    @Transactional
    public UnregisterMemberResponse unregisterMember(int memberId, int forumId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("Member not found"));
        Forum forum = forumRepository.findById(forumId).orElseThrow(() -> new NotFoundException("Forum not found"));

        if (!member.isMemberPartOfForum(forum)) throw new BadRequestException("Cannot unregister a member that is not registered in the forum");

        member.unregisterFromForum(forum);
        return new UnregisterMemberResponse(
                member.getId(),
                forum.getId(),
                "Member unregistered from forum successfully"
        );
    }

    public List<ForumMessageResponse> getAllMessagesFromForumWithinAPeriod(int forumId, long periodInSeconds) {
        if (periodInSeconds <= 0) {
            periodInSeconds = config.period();
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusSeconds(periodInSeconds);
        return forumMessageRepository.findByForumIdAndSentAtBetweenOrderBySentAtAsc(forumId, startDate, now).stream()
                .map(message -> {
                    Member member = message.getSender();
                    String fullName = member.getName() + " " + member.getSecondName();
                    return new ForumMessageResponse(
                            message.getId(),
                            message.getForum().getId(),
                            member.getId(),
                            fullName,
                            message.getContent(),
                            message.getSentAt()
                    );
                })
                .toList();
    }

    public ForumMessageResponse sendForumMessage(SendForumMessageRequest req, int memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("User not found"));
        Forum forum = forumRepository.findById(req.forumId()).orElseThrow(() -> new NotFoundException("Forum not found"));

        if (!member.isMemberPartOfForum(forum)) throw new BadRequestException("Member is not registered in forum");

        ForumMessage message = forumMessageRepository.save(req.toMessage(member, forum, req.message()));

        return new ForumMessageResponse(
                message.getId(),
                message.getForum().getId(),
                member.getId(),
                member.getName() + " " + member.getSecondName(),
                message.getContent(),
                message.getSentAt()
        );
    }

    public List<SimpleMemberResponse> getMembersInForum(Integer forumId) {
        return memberRepository.findMembersByForumId(forumId).stream()
                .map(member -> {
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

    public ForumResponse getForum(Integer forumId) {
        Forum forum = forumRepository.findById(forumId).orElseThrow(() -> new NotFoundException("Forum not found"));
        Album album = albumRepository.findByForumId(forumId).orElseThrow(() -> new NotFoundException("Album not found"));

        return new ForumResponse(
                forum.getId(),
                album.getName(),
                album.getOwner().getId(),
                forum.getCreatedAt()
        );
    }

    public List<Forum> getForumByForumMessageNumDesc() {
        return forumRepository.findAllByForumMessageNumDesc();
    }
}
