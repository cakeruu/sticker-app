import { Album, Users } from 'lucide-react';
import { SectionGroup } from '@/components/section-group';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { CheckStickersInCollectionResponse } from '@/lib/types';

export default function Collection ({ collection } : { collection: CheckStickersInCollectionResponse}) {
  return (
    <div className='container mx-auto py-8 space-y-6'>
      <div className='grid grid-cols-1 md:grid-cols-2 gap-6'>
        <Card>
          <CardHeader className='flex flex-row items-center space-x-4'>
            <Album className='w-8 h-8 text-primary' />
            <CardTitle>Album #{collection.albumId}</CardTitle>
          </CardHeader>
          <CardContent>
            {
              collection.stickers.map((section) => (
                <div key={section.sectionId}>
                  {section.sectionName}: {section.stickersNotCollected.length} / {section.stickersNotCollected.length + section.stickersCollected.length}
                </div>
              ))
            }
          </CardContent>
        </Card>

        <Card>
          <CardHeader className='flex flex-row items-center space-x-4'>
            <Users className='w-8 h-8 text-primary' />
            <CardTitle>Member #{collection.memberId}</CardTitle>
          </CardHeader>
          <CardContent>
            <p className='text-muted-foreground'>
              Total Stickers: {collection.stickers.reduce(
              (acc, section) => acc + section.stickersCollected.length,
              0
            )}
            </p>
          </CardContent>
        </Card>
      </div>

      <div className='space-y-4'>
        {collection.stickers.map((section) => (
          <SectionGroup key={section.sectionId} section={section} collectionId={collection.collectionId} />
        ))}
      </div>
    </div>
  );
}
