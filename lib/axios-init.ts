import { getCookies } from '@/lib/server-utils';
import axios from 'axios';

const api = axios.create({
  baseURL: 'https://ec2-18-193-120-116.eu-central-1.compute.amazonaws.com/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true
});

api.interceptors.response.use(
  response => response,
  error => {
    if (error.response) {
      return Promise.reject(error);
    }
    return Promise.reject(error);
  }
);

api.interceptors.request.use(async (config) => {
  const cookies = await getCookies();
  const token = cookies.find((cookie) => cookie.name === 'token')?.value;

  if (token) {
    config.headers.Cookie = `token=${token}`;
  }

  return config;
});

export default api;
