import { clsx, type ClassValue } from 'clsx';
import { twMerge } from 'tailwind-merge';
import jwt from 'jsonwebtoken';

export function cn (...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export type Token = {
  id: number;
  user_full_name: string;
  user_name: string;
  email: string;
  roles: string;
}

export function getTokenFromLocalStorage (): Token | undefined {
  const token = localStorage.getItem('token');
  if (token) {
    return JSON.parse(JSON.stringify(jwt.decode(token)));
  }
}

export type Sticker = {
  id: number;
  number: number;
  name: string;
  description: string;
  image: string;
  isCollected: boolean;
};

type Section = {
  sectionId: number;
  sectionName: string;
  stickers: Sticker[];
};

// Transformation function
export function flattenSection (section: {
  sectionId: number;
  sectionName: string;
  stickersCollected: Array<{
    id: number;
    number: number;
    name: string;
    description: string;
    image: string;
  }>;
  stickersNotCollected: Array<{
    id: number;
    number: number;
    name: string;
    description: string;
    image: string;
  }>;
}): Section {
  return {
    sectionId: section.sectionId,
    sectionName: section.sectionName,
    stickers: [
      ...section.stickersCollected.map(sticker => ({
        ...sticker,
        isCollected: true
      })),
      ...section.stickersNotCollected.map(sticker => ({
        ...sticker,
        isCollected: false
      }))
    ]
  };
}
