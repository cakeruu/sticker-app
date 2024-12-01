'use server';

import { addStickerToCollection, sendDirectMessage, sendForumMessage, updateProfile } from '@/lib/api';
import { revalidatePath } from 'next/cache';
import { FormDataUpdate } from './update-profile-form';

export async function sendForumMessageAction (forumId: number, content: string) {
  const response = await sendForumMessage({
    forumId,
    message: content
  });
  return { error: response.error, success: response.success };
}

export async function sendDirectMessageAction (forumId: number, receiverId: number, content: string) {
  const response = await sendDirectMessage(forumId, {
    receiverId,
    isOneTime: false,
    content
  });
  return { error: response.error, success: response.success };
}

export async function collectStickerAction (collectionId: number, stickerId: number) {
  const response = await addStickerToCollection(collectionId, stickerId);
  revalidatePath(`/collections/${collectionId}`);
  return { error: response.error, success: response.success };
}

export async function updateProfileAction (data: FormDataUpdate, dateOfBirth: string) {
  const response = await updateProfile({
    email: data.email,
    password: data.password!,
    name: data.name,
    secondName: data.secondName,
    dateOfBirth
  });
  return { error: response.error, success: response.success };
}
