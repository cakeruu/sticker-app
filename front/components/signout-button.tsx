'use client';
import { Button } from './ui/button';
import { CircleArrowLeft } from 'lucide-react';
import { useRouter } from 'next/navigation';
import { signOutAction } from '@/lib/sign-out-action';

export default function SignOutButton () {
  const router = useRouter();

  async function handleSignOut () {
    await signOutAction();
    window.localStorage.removeItem('token');
    router.push('/auth/login');
  }

  return (
    <Button onClick={handleSignOut} variant='outline' className='justify-center rounded-full w-9 h-9'>
      <CircleArrowLeft className='w-5 h-5' />
    </Button>
  );
}
