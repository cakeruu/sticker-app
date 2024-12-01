'use server';
import { cookies as readCookies } from 'next/headers';
import { Token } from './utils';
import jwt from 'jsonwebtoken';

export async function getCookies () {
  const cookies = await readCookies();
  return cookies.getAll();
}

export async function getToken (): Promise<Token | undefined> {
  const cookies = await getCookies();
  const token = cookies.find((cookie) => cookie.name === 'token')?.value;
  if (token) {
    return JSON.parse(JSON.stringify(jwt.decode(token)));
  }
}
