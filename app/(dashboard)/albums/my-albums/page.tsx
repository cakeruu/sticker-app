import Album from '@/components/album';
import ErrorComponent from '@/components/error-client';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { getAlbumsByMember } from '@/lib/api';
import Link from 'next/link';

export default async function MyAlbumsPage () {
  const { success: albums, error } = await getAlbumsByMember();

  if (error) {
    return <ErrorComponent error={error!} />;
  }

  if (!albums) return null;

  return (
    <div className='p-5'>
      <h1 className='text-3xl font-bold mb-8'>My Albums</h1>
      <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6'>
        {
            albums.map((album) => (
              <Link href={`/albums/${album.id}`} key={album.id}>
                <Album album={album} lock />
              </Link>
            ))
        }
        {
            albums.length === 0 && (
              <Link href='/albums/create'>
                <Card className='hover:bg-accent/50 transition-colors'>
                  <CardHeader>
                    <CardTitle className='text-center'>
                      You still haven't created any album
                    </CardTitle>
                    <CardContent className='p-0'>
                      <p className='text-sm text-center text-muted-foreground'>
                        Click here to create an album
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
