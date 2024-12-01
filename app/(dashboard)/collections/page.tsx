import ErrorComponent from '@/components/error-client';
import { getMemberCollections } from '@/lib/api';
import Link from 'next/link';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Calendar, User } from 'lucide-react';
import { format } from 'date-fns';

export default async function CollectionsPage () {
  const { success: collections, error } = await getMemberCollections();
  if (error) {
    return <ErrorComponent error={error} />;
  }
  return (
    <div className='p-5'>
      <h1 className='text-3xl font-bold mb-8'>My Collections</h1>
      <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-2'>
        {collections?.map(collection => (
          <Link href={`collections/${collection.id}`} key={collection.id}>
            <Card className='h-full hover:shadow-lg cursor-pointer max-w-fit hover:scale-105 transition-transform duration-300'>
              <CardHeader>
                <div className='flex justify-between items-center'>
                  <CardTitle className='text-xl'>{collection.album.name}</CardTitle>
                </div>
                <CardDescription className='flex items-center gap-2'>
                  <User className='h-4 w-4' />
                  {collection.album.ownerFullName}
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className='flex items-center gap-2 text-sm text-muted-foreground mb-2'>
                  <Calendar className='h-4 w-4' />
                  <span>
                    {format(new Date(collection.album.beginningDate), 'MMM d, yyyy')} - {format(new Date(collection.album.endingDate), 'MMM d, yyyy')}
                  </span>
                </div>
                <div className='text-sm text-muted-foreground'>
                  Editor: {collection.album.editor}
                </div>
              </CardContent>
            </Card>
          </Link>
        ))}
      </div>
    </div>
  );
}
