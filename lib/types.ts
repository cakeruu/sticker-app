export interface ErrorResponse {
  message: string;
  statusCode: number;
}

export interface ApiResponse<T> {
  success?: T;
  error?: ErrorResponse;
}

export interface Sticker {
  id: number;
  number: number;
  name: string;
  description: string;
  image: string;
}

export interface StickerToSend {
  number: number;
  name: string;
  description: string;
  image: string;
}

export interface Section {
  name: string;
  stickers: StickerToSend[];
}

export interface Section2 {
  name: string;
  stickers: Sticker[];
}

export interface CreateAlbumRequest {
  name: string;
  beginningDate: string;
  endingDate: string;
  editor: string;
  isPublic: boolean;
  sections: Section[];
}

export interface CreateAlbumResponse {
  id: number;
  message: string;
}

export interface AlbumResponse {
  id: number;
  name: string;
  beginningDate: string;
  endingDate: string;
  editor: string;
  ownerId: number;
  ownerFullName: string;
  forumId: number;
  isPublic: boolean;
}

export interface SimpleMemberResponse {
  id: number;
  fullName: string;
  email: string;
  dateOfBirth: string;
  dateOfRegistration: string;
}

export interface ForumResponse {
  id: number;
  albumName: string;
  ownerId: number;
  createdAt: string;
}

export interface GetAlbumWithDetailsResponse {
  id: number;
  name: string;
  beginningDate: string;
  endingDate: string;
  editor: string;
  owner: SimpleMemberResponse;
  forum: ForumResponse;
  isPublic: boolean;
  sections: Section2[];
}

export interface ChangeAlbumVisibilityResponse {
  albumId: number;
  isPublic: boolean;
  message: string;
}

export interface ForumMessageResponse {
  id: number;
  forumId: number;
  senderId: number;
  senderFullName: string;
  content: string;
  sentAt: string;
}

export interface DirectMessageResponse {
  id: number;
  senderId: number;
  senderFullName: string;
  receiverId: number;
  receiverFullName: string;
  isOneTime: boolean;
  isRead: boolean;
  content: string;
  sentAt: string;
}

export interface CollectStickerResponse {
  memberId: number;
  collectionId: number;
  stickerId: number;
  message: string;
}

export interface SendForumMessageRequest {
  forumId: number;
  message: string;
}

export interface SendForumMessageResponse {
  id: number;
  message: string;
}

export interface SendDirectMessageRequest {
  receiverId: number;
  isOneTime: boolean;
  content: string;
}

export interface SendDirectMessageResponse {
  messageId: number;
  senderId: number;
  receiverId: number;
  date: string;
  message: string;
}

export interface MemberResponse {
  password: string;
  name: string;
  secondName: string;
  email: string;
  dateOfBirth: string;
}

export interface SectionStickerGroupResponse {
  sectionId: number;
  sectionName: string;
  stickers: Sticker[];
}

export interface SectionStickerCollectedGroupResponse {
  sectionId: number;
  sectionName: string;
  stickersCollected: Sticker[];
  stickersNotCollected: Sticker[];
}

export interface CreateMemberRequest {
  password: string;
  name: string;
  secondName: string;
  email: string;
  dateOfBirth: string;
  role: 'FREE' | 'PREMIUM' | 'ADMIN';
}

export interface CreateMemberResponse {
  id: number;
  message: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  message: string;
}

export interface UpdateMemberRequest {
  password: string;
  name: string;
  secondName: string;
  email: string;
  dateOfBirth: string;
}
export interface UpdateMemberResponse {
  id: number;
  token: string;
  message: string;
}

export interface CollectionResponse {
  id: number;
  memberId: number;
  albumId: number;
  albumName: string;
  stickers: SectionStickerGroupResponse[];
}

export interface StartAlbumCollectionResponse {
  collectionId: number;
  message: string;
}

export interface ReadForumMessageResponse {
  memberId: number;
  forumId: number;
  messageId: number;
  message: string;
}

export interface ReadDirectMessageResponse {
  id: number;
  senderId: number;
  receiverId: number;
  message: string;
}

export interface CheckStickersInCollectionResponse {
  albumId: number;
  memberId: number;
  collectionId: number;
  albumName: string;
  stickers: SectionStickerCollectedGroupResponse[];
}

export interface ChatResponse {
  firstMemberId: number;
  secondMemberId: number;
  firstMemberName: string;
  secondMemberFullName: string;
  messages: DirectMessageResponse[];
}

export interface SimpleCollectionResponse {
  id: number;
  memberId: number;
  album: AlbumResponse;
}
