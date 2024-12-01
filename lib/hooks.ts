import { useEffect, useState } from 'react';
import { getTokenFromLocalStorage, Token } from './utils';

export function useGetTokenFromLocalStorage () {
  const [token, setToken] = useState<Token | undefined>(undefined);

  useEffect(() => {
    const token = getTokenFromLocalStorage();
    if (token) {
      setToken(token);
    }
  }, []);

  return token;
}
