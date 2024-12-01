'use client';

import { AxiosError } from 'axios';
import { ErrorResponse, LoginRequest, LoginResponse } from './types';
import api from './axios-init';

export async function memberLogin (data: LoginRequest): Promise<{ success?: LoginResponse; error?: ErrorResponse }> {
  try {
    const response = await api.post('/auth/members/login', data);
    return { success: response.data as LoginResponse };
  } catch (err) {
    const error = err as AxiosError;
    return { error: error.response?.data as ErrorResponse };
  }
}
