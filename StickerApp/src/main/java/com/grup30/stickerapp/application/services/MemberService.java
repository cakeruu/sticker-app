package com.grup30.stickerapp.application.services;

import com.grup30.stickerapp.application.dto.album.responses.StartAlbumCollectionResponse;
import com.grup30.stickerapp.application.dto.forum.responses.ForumMessageResponse;
import com.grup30.stickerapp.application.dto.forum.responses.ForumResponse;
import com.grup30.stickerapp.application.dto.member.requests.CreateMemberRequest;
import com.grup30.stickerapp.application.dto.member.requests.LoginRequest;
import com.grup30.stickerapp.application.dto.member.requests.SendDirectMessageRequest;
import com.grup30.stickerapp.application.dto.member.requests.UpdateMemberRequest;
import com.grup30.stickerapp.application.dto.member.responses.*;
import com.grup30.stickerapp.application.exceptions.BadRequestException;
import com.grup30.stickerapp.application.exceptions.NotFoundException;
import com.grup30.stickerapp.config.Config;
import com.grup30.stickerapp.config.ConfigService;
import com.grup30.stickerapp.config.security.JwtService;
import com.grup30.stickerapp.config.security.SecurityUser;
import com.grup30.stickerapp.domain.*;
import com.grup30.stickerapp.domain.enums.Role;
import com.grup30.stickerapp.persistence.*;
import com.grup30.stickerapp.utils.Utils;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.grup30.stickerapp.utils.Utils.generateExtraClaims;
import static com.grup30.stickerapp.utils.Utils.parseAlbum;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final CollectionRepository collectionRepository;
    private final AlbumRepository albumRepository;
    private final ForumRepository forumRepository;
    private final ForumMessageRepository forumMessageRepository;
    private final DirectMessageRepository directMessageRepository;
    private final JwtService jwtService;
    private final Config config;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public MemberService(
            MemberRepository memberRepository,
            CollectionRepository collectionRepository,
            AlbumRepository albumRepository,
            ForumRepository forumRepository,
            ForumMessageRepository forumMessageRepository,
            DirectMessageRepository directMessageRepository,
            JwtService jwtService, ConfigService configService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.memberRepository = memberRepository;
        this.collectionRepository = collectionRepository;
        this.albumRepository = albumRepository;
        this.forumRepository = forumRepository;
        this.forumMessageRepository = forumMessageRepository;
        this.directMessageRepository = directMessageRepository;
        this.jwtService = jwtService;
        this.config = configService.getAppConfig().defaultConfig();
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public CreateMemberResponse createMember(CreateMemberRequest req) {
        String passwordEncoded = passwordEncoder.encode(req.password());
        Member member = memberRepository.save(req.toMember(passwordEncoded));
        return new CreateMemberResponse(
                member.getId(),
                "Member successfully created"
        );
    }

    public ResponseEntity<LoginResponse> memberLogin(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );
        var member = memberRepository.findByEmail(req.email())
                .orElseThrow(() -> new NotFoundException("Member not found"));

        Map<String, Object> extraClaims = generateExtraClaims(new SecurityUser(member));

        var token = jwtService.generateToken(extraClaims, new SecurityUser(member));

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true) // Set to true in production
                .path("/")
                // .domain(".uks1.devtunnels.ms")
                .sameSite("None")
                .maxAge(config.token_exp())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(
                        token,
                        "Member successfully logged in"
                ));
    }

    public UpdateMemberResponse updateMemberProfile(int id, UpdateMemberRequest req) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Member not found"));
        String hashedPassword = passwordEncoder.encode(req.password());
        member.updateProfile(req, hashedPassword);
        memberRepository.save(member);
        return new UpdateMemberResponse(
                id,
                "Member profile updated successfully"
        );
    }

    public MemberResponse getMemberInfo(Integer memberId) {
        var member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("Member not found"));
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getSecondName(),
                member.getEmail(),
                member.getDateOfBirth()
        );
    }

    public List<Member> getAllMembers(String sort) {
        if (sort == null || sort.equals("asc")) return memberRepository.findAllByOrderBySecondNameAsc();
        if (sort.equals("desc")) return memberRepository.findAllByOrderBySecondNameDesc();
        return memberRepository.findAllByOrderBySecondNameAsc();
    }


    public List<SimpleMemberResponse> getAllMembersWithinAPeriod(Long periodInSeconds, String sort) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusSeconds(periodInSeconds);
        if (sort == null || sort.equals("asc")){
            return memberRepository.findByDateOfRegistrationBetweenOrderBySecondNameAsc(startDate, now).stream()
                    .map(member -> new SimpleMemberResponse(
                            member.getId(),
                            member.getName() + " " + member.getSecondName(),
                            member.getEmail(),
                            member.getDateOfBirth(),
                            member.getDateOfRegistration()
                    ))
                    .toList();
        }
        if (sort.equals("desc"))
            return memberRepository.findByDateOfRegistrationBetweenOrderBySecondNameDesc(startDate, now).stream()
                    .map(member -> new SimpleMemberResponse(
                            member.getId(),
                            member.getName() + " " + member.getSecondName(),
                            member.getEmail(),
                            member.getDateOfBirth(),
                            member.getDateOfRegistration()
                    ))
                    .toList();
        return memberRepository.findByDateOfRegistrationBetweenOrderBySecondNameAsc(startDate, now).stream()
                .map(member -> new SimpleMemberResponse(
                        member.getId(),
                        member.getName() + " " + member.getSecondName(),
                        member.getEmail(),
                        member.getDateOfBirth(),
                        member.getDateOfRegistration()
                ))
                .toList();
    }

    public List<SimpleCollectionResponse> getMemberCollections(int id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Member not found"));

        List<Collection> sortedCollections = member.getCollections().stream()
                .sorted(Comparator.comparing(collection -> collection.getAlbum().getEndingDate()))
                .toList();

        return parseCollections(sortedCollections);
    }

    private List<SimpleCollectionResponse> parseCollections(List<Collection> collections) {
        return collections.stream()
                .map(collection -> {
                    Album album = collection.getAlbum();
                    return new SimpleCollectionResponse(
                            collection.getId(),
                            collection.getMember().getId(),
                            parseAlbum(album, Utils::mapToAlbumResponse)
                    );
                })
                .toList();
    }

    @Transactional
    public StartAlbumCollectionResponse startAlbumCollection(int id, int albumId) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Member not found"));
        Album album = albumRepository.findById(albumId).orElseThrow(() -> new NotFoundException("Album not found"));

        var role = member.getRole();

        if (role.equals(Role.FREE) && member.getCollections().size() == 1) {
            throw new BadRequestException("You have reached the maximum number of collections for subscription");
        }

        Collection existingCollection = member.getCollections().stream()
                .filter(collection -> collection.getAlbum().getId() == albumId)
                .findFirst()
                .orElse(null);
        if (existingCollection != null) throw new BadRequestException("You already have a collection for this album");

        if (!album.isPublicAndActive())
            throw new NotFoundException("Album not found"); // Throwing NotFoundException to not expose the existence of the album
        Collection collection = collectionRepository.save(new Collection(member, album));

        member.registerToForum(collection.getAlbum().getForum()); // Register member to forum when starting a collection

        return new StartAlbumCollectionResponse(
                albumId,
                id,
                collection.getId(),
                "Great! You have started a collection " + album.getName() + " album!"
        );
    }

    public List<CollectionResponse> getActiveCollections(int id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Member not found"));
        List<Collection> collections = member.getCollections();
        return collections.stream()
                .filter(Collection::isActiveAndPublic)
                .map(Utils::parseCollection)
                .toList();
    }

    @Transactional
    public CollectStickerResponse addStickerToCollection(int id, int collectionId, int stickerId) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Member not found"));

        Collection collection = member.getCollections().stream()
                .filter(col -> col.getId() == collectionId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Collection not found"));

        collection.getAlbum().getSections().stream()
                .flatMap(section -> section.getStickers().stream())
                .forEach(st -> System.out.println("StickerID: " + st.getId()));

        Sticker sticker = collection.getAlbum().getSections().stream()
                .flatMap(section -> section.getStickers().stream())
                .filter(st -> st.getId() == stickerId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Sticker not found"));

        if (!collection.isActiveAndPublic())
            throw new BadRequestException("Collection not available");

        collection.addSticker(sticker);
        // collectionRepository.save(collection);

        return new CollectStickerResponse(
                id,
                collectionId,
                stickerId,
                "Sticker successfully added to collection"
        );
    }

    public List<DirectMessageResponse> getUnreadDirectMessagesFromForum(int id, int forumId) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Member not found"));
        Forum forum = forumRepository.findById(forumId).orElseThrow(() -> new NotFoundException("Forum not found"));

        if (!member.isMemberPartOfForum(forum)) throw new BadRequestException("Member is not registered in forum");

        List<DirectMessage> unreadMessages = directMessageRepository.findByReceiverAndForumAndIsReadIsFalse(member, forum);

        return unreadMessages.stream()
                .map(this::parseDirectMessage)
                .toList();
    }

    public ReadForumMessageResponse markForumMessageAsRead(int id, int forumId, int messageId) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Member not found"));
        Forum forum = forumRepository.findById(forumId).orElseThrow(() -> new NotFoundException("Forum not found"));

        List<ForumMessage> messages = forumMessageRepository.findAll();

        System.out.println("MessageSnt: " + messageId);

        messages.forEach(message ->
                System.out.println("MessageID: " + message.getId())
                );

        ForumMessage message = forumMessageRepository.findById(messageId).orElseThrow(() -> new NotFoundException("Message not found"));

        if (!member.isMemberPartOfForum(forum)) throw new BadRequestException("Member is not registered in forum");
        if (!message.isInForum(forum)) throw new BadRequestException("Message is not in the forum");

        message.markAsReadBy(member.getId());
        forumMessageRepository.save(message);

        return new ReadForumMessageResponse(
                id,
                forumId,
                messageId,
                "Message read successfully"
        );
    }

    public DirectMessageResponse sendDirectMessageInForum(int id, int forumId, SendDirectMessageRequest req) {
        Member sender = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Sender not found"));
        Member receiver = memberRepository.findById(req.receiverId()).orElseThrow(() -> new NotFoundException("Receiver not found"));
        Forum forum = forumRepository.findById(forumId).orElseThrow(() -> new NotFoundException("Forum not found"));

        var senderRole = sender.getRole();
        if (senderRole.equals(Role.FREE)) {
            throw new BadRequestException("You need to be a premium member to send direct messages");
        }

        var receiverRole = receiver.getRole();
        if (receiverRole.equals(Role.FREE)) {
            throw new BadRequestException("Receiver needs to be a premium member to receive direct messages");
        }

        // Check if receiver is blocked by sender
        if (sender.isMemberBlocked(receiver)) throw new BadRequestException("Receiver is blocked by the sender");
        // Check if sender is blocked by receiver
        if (receiver.isMemberBlocked(sender)) throw new BadRequestException("Sender is blocked by the receiver");
        if (!sender.isMemberPartOfForum(forum)) throw new BadRequestException("Member is not registered in forum");
        if (!receiver.isMemberPartOfForum(forum)) throw new BadRequestException("Receiver is not registered in forum");

        DirectMessage message = directMessageRepository.save(req.toDirectMessage(sender, receiver, forum));

        return parseDirectMessage(message);
    }

    public List<ForumMessageResponse> getUnreadMessagesFromForum(int id, int forumId) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Member not found"));
        Forum forum = forumRepository.findById(forumId).orElseThrow(() -> new NotFoundException("Forum not found"));

        if (!member.isMemberPartOfForum(forum)) throw new BadRequestException("Member is not registered in forum");

        List<ForumMessage> unreadMessages = forumMessageRepository.findByForum(forum);

        return unreadMessages.stream()
                .filter(message -> !message.isReadBy(member.getId()))
                .map(this::parseForumMessage)
                .toList();
    }

    public ReadDirectMessageResponse markDirectMessageAsRead(int id, int forumId, int messageId) {
        DirectMessage message = directMessageRepository.findById(messageId).orElseThrow(() -> new NotFoundException("Message not found"));

        if (message.getReceiver().getId() != id) throw new BadRequestException("Member is not the receiver of the message");
        if (!message.isInForum(forumId)) throw new BadRequestException("Message is not in the forum");

        message.markAsRead();
        directMessageRepository.save(message);

        return new ReadDirectMessageResponse(
                id,
                forumId,
                messageId,
                "Message read successfully"
        );
    }

    public List<ForumResponse> getMemberForums(int id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Member not found"));
        List<Forum> forums = member.getForums();
        return forums.stream()
                .map(forum -> {
                    Album album = albumRepository.findByForumId(forum.getId()).orElseThrow(() -> new NotFoundException("Album not found"));
                    return new ForumResponse(
                            forum.getId(),
                            album.getName(),
                            album.getOwner().getId(),
                            forum.getCreatedAt()
                    );
                })
                .toList();
    }

    public List<ChatResponse> getAllChatsFromForum(Integer id, Integer forumId) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found"));
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new NotFoundException("Forum not found"));

        if (!member.isMemberPartOfForum(forum)) throw new BadRequestException("Member is not registered in forum");

        List<Member> forumMembers = memberRepository.findMembersByForumId(forumId)
                .stream()
                .filter(m -> !m.getId().equals(id))
                .toList();

        List<ChatResponse> chatResponses = new ArrayList<>();

        for (Member otherMember : forumMembers) {
            List<DirectMessage> directMessages = directMessageRepository.findDirectMessagesBetweenMembers(
                    id,
                    otherMember.getId(),
                    forumId
            );

            if (!directMessages.isEmpty()) {
                ChatResponse chatResponse = new ChatResponse(
                        id,
                        otherMember.getId(),
                        member.getName() + " " + member.getSecondName(),
                        otherMember.getName() + " " + otherMember.getSecondName(),
                        directMessages.stream()
                                .map(this::parseDirectMessage)
                                .toList()
                );
                chatResponses.add(chatResponse);
            }
        }

        return chatResponses;
    }

    public CheckStickersInCollectionResponse checkStickersInCollection(int id, int collectionId) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("Member not found"));
        Collection collection = findActivePublicCollection(member, collectionId);
        Album album = collection.getAlbum();

        List<SectionStickerCollectedGroupResponse> sections = buildSectionResponses(album, collection.getStickersCollected());

        return new CheckStickersInCollectionResponse(
                album.getId(),
                member.getId(),
                collection.getId(),
                album.getName(),
                sections
        );
    }

    @Transactional
    public BlockMemberResponse blockMember(int memberId, int otherMemberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("Member not found"));
        Member othermember = memberRepository.findById(otherMemberId).orElseThrow(() -> new NotFoundException("Other member not found"));

        member.blockMember(othermember);

        return new BlockMemberResponse(
                otherMemberId,
                "Member was blocked successfully!"
        );
    }

    @Transactional
    public UnblockMemberResponse unblockMember(int memberId, int otherMemberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("Member not found"));
        Member othermember = memberRepository.findById(otherMemberId).orElseThrow(() -> new NotFoundException("Other member not found"));

        member.unblockMember(othermember);

        return new UnblockMemberResponse(
                otherMemberId,
                "Member was unblocked successfully!"
        );
    }

    public List<SimpleMemberResponse> getMembersByMessageNumDesc() {
        return memberRepository.findAllByMessageNumDesc().stream()
                .map(member -> new SimpleMemberResponse(
                        member.getId(),
                        member.getName() + " " + member.getSecondName(),
                        member.getEmail(),
                        member.getDateOfBirth(),
                        member.getDateOfRegistration()
                ))
                .toList();
    }

    // private functions

    private ForumMessageResponse parseForumMessage(ForumMessage message) {
        Member sender = message.getSender();
        String senderFullName = sender.getName() + " " + sender.getSecondName();
        return new ForumMessageResponse(
                message.getId(),
                message.getForum().getId(),
                message.getSender().getId(),
                senderFullName,
                message.getContent(),
                message.getSentAt()
        );
    }

    private DirectMessageResponse parseDirectMessage(DirectMessage directMessage) {
        Member sender = directMessage.getSender();
        String senderFullName = sender.getName() + " " + sender.getSecondName();
        Member receiver = directMessage.getReceiver();
        String receiverFullName = receiver.getName() + " " + receiver.getSecondName();
        return new DirectMessageResponse(
                directMessage.getId(),
                sender.getId(),
                senderFullName,
                receiver.getId(),
                receiverFullName,
                directMessage.isRead(),
                directMessage.isOnetime(),
                directMessage.getContent(),
                directMessage.getSentAt()
        );
    }

    private Collection findActivePublicCollection(Member member, int collectionId) {
        System.out.println("CollectionENter: " + collectionId);
        member.getCollections().stream()
                .forEach(col -> System.out.println("CollectionID: " + col.getId()));

        Collection collection = member.getCollections().stream()
                .filter(col -> col.getId() == collectionId)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Collection not found"));

        if (!collection.isActiveAndPublic()) {
            throw new NotFoundException("Collection not found"); // Hide existence of the collection
        }
        return collection;
    }

    private List<SectionStickerCollectedGroupResponse> buildSectionResponses(Album album, List<Sticker> stickersCollected) {
        return album.getSections().stream()
                .map(section -> {
                    List<Sticker> sectionStickers = section.getStickers();

                    List<Sticker> stickersInCollection = sectionStickers.stream()
                            .filter(stickersCollected::contains)
                            .toList();
                    List<Sticker> stickersNotCollected = sectionStickers.stream()
                            .filter(sticker -> !stickersCollected.contains(sticker))
                            .toList();

                    return new SectionStickerCollectedGroupResponse(
                            section.getId(),
                            section.getName(),
                            stickersInCollection,
                            stickersNotCollected
                    );
                })
                .toList();
    }
}