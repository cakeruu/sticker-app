import { getCookies } from '@/lib/server-utils';
import axios from 'axios';
import https from 'https';

const api = axios.create({
  baseURL: 'https://18.193.120.116/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true,
  httpsAgent: new https.Agent({
    rejectUnauthorized: false
  })
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
