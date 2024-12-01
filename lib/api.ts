'use server';
import { AxiosError } from 'axios';
import api from './axios-init';
import {
  ChangeAlbumVisibilityResponse,
  ChatResponse, CreateAlbumRequest,
  CreateAlbumResponse,
  CreateMemberRequest,
  CreateMemberResponse,
  DirectMessageResponse,
  ErrorResponse,
  ForumMessageResponse,
  ForumResponse,
  AlbumResponse,
  GetAlbumWithDetailsResponse,
  CollectionResponse,
  SendDirectMessageRequest,
  SendDirectMessageResponse,
  SendForumMessageRequest,
  SendForumMessageResponse,
  SimpleMemberResponse,
  StartAlbumCollectionResponse,
  CheckStickersInCollectionResponse,
  ApiResponse,
  SimpleCollectionResponse,
  CollectStickerResponse,
  UpdateMemberRequest,
  UpdateMemberResponse,
  MemberResponse
} from './types';

// Forum API
export async function getAllMessagesFromForum (forumId: number): Promise<ApiResponse<ForumMessageResponse[]>> {
  try {
    const response = await api.get(`/forums/${forumId}/messages`);
    return { success: response.data as ForumMessageResponse[] };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function getUnreadMessagesFromForum (forumId: number): Promise<ApiResponse<ForumMessageResponse[]>> {
  try {
    const response = await api.get(`/members/forum=${forumId}/unread-forum-messages`);
    return { success: response.data as ForumMessageResponse[] };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function getUnreadDirectMessages (forumId: number): Promise<ApiResponse<DirectMessageResponse[]>> {
  try {
    const response = await api.get(`/members/forum=${forumId}/unread-direct-messages`);
    return { success: response.data as DirectMessageResponse[] };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function getMembersInForum (forumId: number): Promise<ApiResponse<SimpleMemberResponse[]>> {
  try {
    const response = await api.get(`/forums/${forumId}/members`);
    return { success: response.data };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function getForum (forumId: number): Promise<ApiResponse<ForumResponse>> {
  try {
    const response = await api.get(`/forums/${forumId}`);
    return { success: response.data as ForumResponse };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function sendForumMessage (data: SendForumMessageRequest): Promise<ApiResponse<SendForumMessageResponse>> {
  try {
    const response = await api.post('/forums/messages', data);
    return { success: response.data as SendForumMessageResponse };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

// Album API
export async function createAlbum (data: CreateAlbumRequest): Promise<ApiResponse<CreateAlbumResponse>> {
  try {
    const response = await api.post('/albums', data);
    return { success: response.data as CreateAlbumResponse };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function getAlbums (): Promise<ApiResponse<AlbumResponse[]>> {
  try {
    const response = await api.get('/albums');
    return { success: response.data as AlbumResponse[] };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function getAvailableAlbums (): Promise<ApiResponse<AlbumResponse[]>> {
  try {
    const response = await api.get('/albums/available');
    return { success: response.data as AlbumResponse[] };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function getAlbumWithDetails (id: number): Promise<ApiResponse<GetAlbumWithDetailsResponse>> {
  try {
    const response = await api.get(`/albums/${id}/details`);
    return { success: response.data as GetAlbumWithDetailsResponse };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function getAlbumsByMember (): Promise<ApiResponse<AlbumResponse[]>> {
  try {
    const response = await api.get('/albums/member');
    return { success: response.data as AlbumResponse[] };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function changeAlbumVisibility (id: number, visibility: boolean): Promise<ApiResponse<ChangeAlbumVisibilityResponse>> {
  try {
    const response = await api.put(`/albums/${id}/isPublic=${visibility}`);
    return { success: response.data as ChangeAlbumVisibilityResponse };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

// Member API
export async function getMemberCollections (): Promise<ApiResponse<SimpleCollectionResponse[]>> {
  try {
    const response = await api.get('/members/collections');
    return { success: response.data };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function updateProfile (data: UpdateMemberRequest): Promise<ApiResponse<UpdateMemberResponse>> {
  try {
    const response = await api.put('/members/profile', data);
    return { success: response.data as UpdateMemberResponse };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function getMemberInfo (): Promise<ApiResponse<MemberResponse>> {
  try {
    const response = await api.get('/members/profile');
    return { success: response.data as MemberResponse };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function addStickerToCollection (collectionId: number, stickerId: number): Promise<ApiResponse<CollectStickerResponse>> {
  try {
    const response = await api.post(`/members/collection=${collectionId}/sticker=${stickerId}`);
    return { success: response.data as CollectStickerResponse };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}
export async function checkStickersInCollection (collectionId: number): Promise<ApiResponse<CheckStickersInCollectionResponse>> {
  try {
    const response = await api.get(`/members/collection=${collectionId}/stickers`);
    return { success: response.data as CheckStickersInCollectionResponse };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function startAlbumCollection (albumId: number): Promise<ApiResponse<StartAlbumCollectionResponse>> {
  try {
    const response = await api.post(`/members/collections/${albumId}`);
    return { success: response.data as StartAlbumCollectionResponse };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function getAllChatsFromForum (id: number): Promise<ApiResponse<ChatResponse[]>> {
  try {
    const response = await api.get(`/members/forum=${id}/direct-messages`);
    return { success: response.data as ChatResponse[] };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function getActiveCollections (): Promise<ApiResponse<CollectionResponse>> {
  try {
    const response = await api.get('/members/collections/active');
    return { success: response.data };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function getMemberForums (): Promise<ApiResponse<ForumResponse[]>> {
  try {
    const response = await api.get('/members/forums');
    return { success: response.data as ForumResponse[] };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function sendDirectMessage (forumId: number, data: SendDirectMessageRequest): Promise<ApiResponse<SendDirectMessageResponse>> {
  try {
    const response = await api.post(`/members/forum=${forumId}/message`, data);
    return { success: response.data as SendDirectMessageResponse };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function createMember (data: CreateMemberRequest): Promise<ApiResponse<CreateMemberResponse>> {
  try {
    const response = await api.post('/auth/members', data);
    return { success: response.data as CreateMemberResponse };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

// Collections API

export async function getCollectorsByAlbumId (albumId: number): Promise<ApiResponse<SimpleMemberResponse[]>> {
  try {
    const response = await api.get(`/collections/album=${albumId}/collectors`);
    return { success: response.data };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}

export async function getCollectionById (id: number): Promise<ApiResponse<CollectionResponse>> {
  try {
    const response = await api.get(`/collections/${id}`);
    return { success: response.data };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}
