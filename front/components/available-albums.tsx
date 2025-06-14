'use client';

import { AlbumResponse } from '@/lib/types';
import Link from 'next/link';
import Album from './album';
import { Card, CardContent, CardHeader, CardTitle } from './ui/card';

type AvailableAlbumsPageProps = {
  availableAlbums: AlbumResponse[];
};
export default function AvailableAlbumsPage ({ availableAlbums }: AvailableAlbumsPageProps) {
  return (
    <div className='p-5'>
      <h1 className='text-3xl font-bold mb-8'>Available Albums</h1>
      <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6'>
        {availableAlbums.map((album) => (
          <Link href={`/albums/${album.id}`} key={album.id}>
            <Album album={album} />
          </Link>
        ))}
        {
          availableAlbums.length === 0 && (
            <Link href='/albums/create'>
              <Card className='hover:bg-accent/50 transition-colors'>
                <CardHeader>
                  <CardTitle className='text-center'>
                    There is not available albums
                  </CardTitle>
                  <CardContent className='p-0'>
                    <p className='text-sm text-center text-muted-foreground'>
                      Click here to create your first album
                    </p>
                  </CardContent>
                </CardHeader>
              </Card>
            </Link>
          )
        }
      </div>
    </div>
  );
}
