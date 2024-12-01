import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';
import api from './lib/axios-init';

export async function middleware (request: NextRequest) {
  const { pathname } = request.nextUrl;

  // Skip verification for public routes and static files
  if (
    pathname.startsWith('/auth') ||
    pathname.startsWith('/_next') ||
    pathname.startsWith('/api') ||
    pathname === '/favicon.ico'
  ) {
    return NextResponse.next();
  }

  const token = request.cookies.get('token')?.value;

  if (!token) {
    console.log('No token found, redirecting to login');
    return NextResponse.redirect(new URL('/auth/login', request.url));
  }

  try {
    const response = await api.post(
      '/auth/verify',
      {},
      {
        headers: {
          Cookie: `token=${token}`
        }
      }
    );

    if (response.status === 200) {
      return NextResponse.next();
    }
  } catch (error) {
    console.error(error);
    const response = NextResponse.redirect(new URL('/auth/login', request.url));
    response.cookies.delete('token');
    return response;
  }

  return NextResponse.redirect(new URL('/auth/login', request.url));
}

export const config = {
  matcher: [
    /*
     * Match all paths except:
     * - api (API routes)
     * - _next (Next.js files)
     * - static files
     * - auth routes
     */
    '/((?!api|_next/static|_next/image|favicon.ico|auth).*)'
  ]
};
