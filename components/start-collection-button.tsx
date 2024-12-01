'use client';
import { startAlbumCollection } from '@/lib/api';
import { Button } from './ui/button';
import { toast } from 'sonner';

export function StartCollectionButton ({ children, albumId }: {children: React.ReactNode, albumId: number}) {
  async function startCollection () {
    const { success, error } = await startAlbumCollection(albumId);

    if (error) {
      toast.error(error.message);
    } else {
      toast.success(success?.message);
    }
  }
  return (
    <Button onClick={startCollection} className='max-w-44'>
      {children}
    </Button>
  );
}
