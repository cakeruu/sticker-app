import { getAlbumWithDetails, changeAlbumVisibility } from '@/lib/api';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Calendar, User, Lock, Unlock, ArrowLeft } from 'lucide-react';
import Link from 'next/link';
import { format } from 'date-fns';
import ErrorComponent from '@/components/error-client';
import { StartCollectionButton } from '@/components/start-collection-button';
import { revalidatePath } from 'next/cache';
import { SectionGroupAlbum } from '@/components/section-group-album';
import { getToken } from '@/lib/server-utils';

export default async function AlbumDetailsPage (props: { params: Promise<{ id: number }> }) {
  const params = await props.params;
  const { id } = params;
  const { success: album, error } = await getAlbumWithDetails(id);
  const token = await getToken();
  const memberId = token?.id;
  const ownerId = album?.owner.id;
  const isOwner = memberId! - ownerId!;
  console.log('isOwner', isOwner);

  async function toggleVisibility () {
    'use server';
    const currentVisibility = album?.isPublic;
    const { error } = await changeAlbumVisibility(id, !currentVisibility);

    if (error) {
      console.error(error);
    } else {
      revalidatePath(`/albums/${id}`);
    }
  }

  if (error) {
    return <ErrorComponent error={error} />;
  }

  if (!album) return null;

  return (
    <div className='container mx-auto py-8'>
      <div className='mb-6'>
        <Link href='/albums'>
          <Button variant='ghost' className='gap-2'>
            <ArrowLeft className='h-4 w-4' />
            Back to Albums
          </Button>
        </Link>
      </div>

      <Card className='mb-8'>
        <CardHeader>
          <div className='flex justify-between items-center'>
            <div>
              <CardTitle className='text-3xl mb-2'>{album.name}</CardTitle>
              <CardDescription className='flex items-center gap-2 text-lg'>
                <User className='h-5 w-5' />
                {album.owner.fullName}
              </CardDescription>
            </div>
            <button onClick={toggleVisibility}>
              {
                isOwner === 0 && (
                  album.isPublic
                    ? (
                      <Unlock className='h-6 w-6 text-primary' />
                      )
                    : (
                      <Lock className='h-6 w-6 text-destructive' />
                      )
                )
              }
            </button>
          </div>
        </CardHeader>
        <CardContent>
          <div className='grid gap-4'>
            <div className='flex items-center gap-2 text-muted-foreground'>
              <Calendar className='h-5 w-5' />
              <span>
                {format(new Date(album.beginningDate), 'MMMM d, yyyy')} - {format(new Date(album.endingDate), 'MMMM d, yyyy')}
              </span>
            </div>

            <StartCollectionButton albumId={album.id}>
              Start collection!
            </StartCollectionButton>

            <div className='text-muted-foreground'>
              Editor: {album.editor}
            </div>

            <div className='space-y-4'>
              {album.sections.map((section) => (
                <SectionGroupAlbum key={section.name} section={section} />
              ))}
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
