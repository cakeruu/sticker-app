package com.grup30.stickerapp.member;

import com.grup30.stickerapp.application.dto.album.requests.CreateAlbumRequest;
import com.grup30.stickerapp.application.dto.album.responses.CreateAlbumResponse;
import com.grup30.stickerapp.application.dto.album.responses.StartAlbumCollectionResponse;
import com.grup30.stickerapp.application.dto.forum.requests.RegisterMemberRequest;
import com.grup30.stickerapp.application.dto.forum.requests.SendForumMessageRequest;
import com.grup30.stickerapp.application.dto.forum.responses.ForumMessageResponse;
import com.grup30.stickerapp.application.dto.forum.responses.ForumResponse;
import com.grup30.stickerapp.application.dto.forum.responses.RegisterMemberResponse;
import com.grup30.stickerapp.application.dto.forum.responses.SendForumMessageResponse;
import com.grup30.stickerapp.application.dto.member.requests.*;
import com.grup30.stickerapp.application.dto.member.responses.*;
import com.grup30.stickerapp.domain.Member;
import com.grup30.stickerapp.domain.Section;
import com.grup30.stickerapp.domain.Sticker;
import com.grup30.stickerapp.domain.enums.*;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;

    private String authToken;

    private boolean firstTime = true;

    private int albumId;
    private int collectionId;
    private int stickerId;
    private int forumId;

    @BeforeEach
    public void setup() {
        if (!firstTime) {
            return;
        }
        firstTime = false;

        baseUrl = "http://localhost:" + port;

        String uniqueEmail = "john.doe+" + UUID.randomUUID() + "@example.com";

        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
                "123456",
                "John",
                "Doe",
                uniqueEmail,
                LocalDateTime.of(1990, 5, 15, 0, 0),
                Role.ADMIN
        );

        ResponseEntity<CreateMemberResponse> createResponse = restTemplate.postForEntity(
                baseUrl + "/api/auth/members",
                createMemberRequest,
                CreateMemberResponse.class
        );

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        // Login the member
        LoginRequest loginRequest = new LoginRequest(
                uniqueEmail,
                "123456"
        );

        ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity(
                baseUrl + "/api/auth/members/login",
                loginRequest,
                LoginResponse.class
        );

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        authToken = loginResponse.getBody().token();

        var stickers = List.of(
                new Sticker("Sticker1", "Description1", "Image1", 1),
                new Sticker("Sticker2", "Description2", "Image2", 4)
        );
        var sections = List.of(
                new Section("Section1", stickers),
                new Section("Section2", stickers)
        );
        CreateAlbumRequest createAlbumRequest = new CreateAlbumRequest(
                "Album 1",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 12, 31, 23, 59),
                "John Doe",
                true,
                sections
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateAlbumRequest> entity = new HttpEntity<>(createAlbumRequest, headers);

        ResponseEntity<CreateAlbumResponse> response = restTemplate.exchange(
                baseUrl + "/api/albums",
                HttpMethod.POST,
                entity,
                CreateAlbumResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());



        RegisterMemberRequest registerMemberRequest = new RegisterMemberRequest(
                1,
                1
        );

        HttpEntity<RegisterMemberRequest> registerEntity = new HttpEntity<>(registerMemberRequest, headers);

        restTemplate.exchange(
                baseUrl + "/api/forums/" + 1 + "/register-member",
                HttpMethod.POST,
                registerEntity,
                RegisterMemberResponse.class
        );

        albumId = response.getBody().id();
        collectionId = 16;
        stickerId = 34;
        forumId = 1;
    }

    @Test
    @Order(1)
    public void testUpdateMemberProfileInvalid() {
        UpdateMemberRequest updateRequest = new UpdateMemberRequest(
                "newPassword123",
                "Jane",
                "Doe",
                "jane@example.com",
                LocalDateTime.of(1991, 6, 20, 0, 0)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UpdateMemberRequest> entity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/members/profile",
                HttpMethod.PUT,
                entity,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(2)
    public void testGetMemberInfoSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<MemberResponse> response = restTemplate.exchange(
                baseUrl + "/api/members/profile",
                HttpMethod.GET,
                entity,
                MemberResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(3)
    public void testGetMemberInfoUnauthorized() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/api/members/profile",
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(4)
    public void testGetMemberCollectionsSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<SimpleCollectionResponse[]> response = restTemplate.exchange(
                baseUrl + "/api/members/collections",
                HttpMethod.GET,
                entity,
                SimpleCollectionResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(5)
    public void testGetMemberCollectionsUnauthorized() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/api/members/collections",
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(7)
    public void testStartAlbumCollectionInvalidAlbum() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        int albumId = -1; // Invalid album ID

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/members/collections/" + albumId,
                HttpMethod.POST,
                entity,
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(8)
    public void testGetActiveCollectionsSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<CollectionResponse[]> response = restTemplate.exchange(
                baseUrl + "/api/members/collections/active",
                HttpMethod.GET,
                entity,
                CollectionResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(9)
    public void testGetActiveCollectionsUnauthorized() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/api/members/collections/active",
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(10)
    public void testAddStickerToCollectionSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<CollectStickerResponse> response = restTemplate.exchange(
                baseUrl + "/api/members/collection=" + collectionId + "/sticker=" + stickerId,
                HttpMethod.POST,
                entity,
                CollectStickerResponse.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(11)
    public void testAddStickerToCollectionInvalidSticker() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        int invalidStickerId = -1;

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/api/members/collection=" + collectionId + "/sticker=" + invalidStickerId,
                HttpMethod.POST,
                entity,
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(12)
    public void testSendDirectMessageInForumSuccess() {
        // First, register the member to the forum
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> registerEntity = new HttpEntity<>(headers);
        int forumId = 1; // Assuming forum with id 1 exists

        restTemplate.exchange(
                baseUrl + "/api/forums/" + forumId + "/register-member",
                HttpMethod.POST,
                registerEntity,
                String.class
        );

        // Create another member to send the message to
        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
                "password123",
                "Jane",
                "Doe",
                "jane.doe@example.com",
                LocalDateTime.of(1992, 6, 20, 0, 0),
                Role.PREMIUM
        );

        ResponseEntity<CreateMemberResponse> createResponse = restTemplate.postForEntity(
                baseUrl + "/api/auth/members",
                createMemberRequest,
                CreateMemberResponse.class
        );

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        int receiverId = createResponse.getBody().id();

        // Register the receiver to the forum
        restTemplate.exchange(
                baseUrl + "/api/forums/" + forumId + "/register-member?memberId=" + receiverId,
                HttpMethod.POST,
                registerEntity,
                String.class
        );

        // Send a direct message
        SendDirectMessageRequest messageRequest = new SendDirectMessageRequest(
                receiverId,
                false,
                "Hello, Jane!"
        );

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SendDirectMessageRequest> entity = new HttpEntity<>(messageRequest, headers);

        ResponseEntity<SendDirectMessageResponse> response = restTemplate.exchange(
                baseUrl + "/api/members/forum=" + forumId + "/message",
                HttpMethod.POST,
                entity,
                SendDirectMessageResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Message sent successfully", response.getBody().message());
    }
    @Test
    @Order(13)
    public void testSendForumMessageSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);


        SendForumMessageRequest messageRequest = new SendForumMessageRequest(
                forumId,
                "Hello, everyone!"
        );

        HttpEntity<SendForumMessageRequest> entity = new HttpEntity<>(messageRequest, headers);

        ResponseEntity<SendForumMessageResponse> response = restTemplate.exchange(
                baseUrl + "/api/forums/messages",
                HttpMethod.POST,
                entity,
                SendForumMessageResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(14)
    public void testMarkForumMessageAsReadSuccess() {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ReadForumMessageResponse> response = restTemplate.exchange(
                baseUrl + "/api/members/forum=" + forumId + "/read-message/" + 1,
                HttpMethod.POST,
                entity,
                ReadForumMessageResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(15)
    public void testGetUnreadMessagesFromForumSuccess() {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ForumMessageResponse[]> response = restTemplate.exchange(
                baseUrl + "/api/members/forum=" + forumId + "/unread-forum-messages",
                HttpMethod.GET,
                entity,
                ForumMessageResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(16)
    public void testGetUnreadDirectMessagesFromForumSuccess() {
        int forumId = 1;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<DirectMessageResponse[]> response = restTemplate.exchange(
                baseUrl + "/api/members/forum=" + forumId + "/unread-direct-messages",
                HttpMethod.GET,
                entity,
                DirectMessageResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(17)
    public void testMarkDirectMessageAsReadInvalidReceiver() {
        // Assuming a direct message with ID 1 exists in forum 1
        int forumId = 1;
        int messageId = 1;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ReadDirectMessageResponse> response = restTemplate.exchange(
                baseUrl + "/api/members/forum=" + forumId + "/read-direct-message/" + messageId,
                HttpMethod.POST,
                entity,
                ReadDirectMessageResponse.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(18)
    public void testCheckStickersInCollectionSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<CheckStickersInCollectionResponse> response = restTemplate.exchange(
                baseUrl + "/api/members/collection=" + 34 + "/stickers",
                HttpMethod.GET,
                entity,
                CheckStickersInCollectionResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(19)
    public void testGetMemberForumsSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ForumResponse[]> response = restTemplate.exchange(
                baseUrl + "/api/members/forums",
                HttpMethod.GET,
                entity,
                ForumResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(20)
    public void testGetAllChatsFromForumSuccess() {
        int forumId = 1;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ChatResponse[]> response = restTemplate.exchange(
                baseUrl + "/api/members/forum=" + forumId + "/direct-messages",
                HttpMethod.GET,
                entity,
                ChatResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(21)
    public void testGetAllMembersFromPeriodSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<SimpleMemberResponse[]> response = restTemplate.exchange(
                baseUrl + "/api/members/recent?days=1",
                HttpMethod.GET,
                entity,
                SimpleMemberResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(22)
    public void testBlockMemberSuccess() {
        // Create another member to block
        CreateMemberRequest createMemberRequest = new CreateMemberRequest(
                "password123",
                "Bob",
                "Brown",
                "bob.brown@example.com",
                LocalDateTime.of(1991, 8, 10, 0, 0),
                Role.FREE
        );

        ResponseEntity<CreateMemberResponse> createResponse = restTemplate.postForEntity(
                baseUrl + "/api/auth/members",
                createMemberRequest,
                CreateMemberResponse.class
        );

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        int otherMemberId = createResponse.getBody().id();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<BlockMemberResponse> response = restTemplate.exchange(
                baseUrl + "/api/members/block/other-member=" + otherMemberId,
                HttpMethod.POST,
                entity,
                BlockMemberResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @Order(24)
    public void testGetMembersByMessageNumDescSuccess() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<SimpleMemberResponse[]> response = restTemplate.exchange(
                baseUrl + "/api/members/active-users",
                HttpMethod.GET,
                entity,
                SimpleMemberResponse[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}