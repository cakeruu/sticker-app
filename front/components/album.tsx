
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Calendar, User, Lock, Unlock } from 'lucide-react';
import { AlbumResponse } from '@/lib/types';
import { format } from 'date-fns';
import { cn } from '@/lib/utils';

export default function Album ({ album, lock = false }: { album: AlbumResponse, lock?: boolean }) {
  return (
    <Card className='h-full hover:shadow-lg cursor-pointer max-w-fit hover:scale-105 transition-transform duration-300'>
      <CardHeader className='border-b p-3'>
        <div className={cn(
          'flex justify-center items-center',
          lock && ' justify-around'
        )}
        >
          <CardTitle className='text-xl overflow-hidden text-ellipsis'>{album.name}</CardTitle>
          {
                        lock &&
                        (
                          album.isPublic
                            ? (
                              <Unlock className='h-5 w-5 text-primary -mt-[0.125rem]' />
                              )
                            : (
                              <Lock className='h-5 w-5 text-destructive -mt-[0.125rem]' />
                              )
                        )
                    }
        </div>
      </CardHeader>
      <CardContent className='flex-none text-muted-foreground p-6 flex flex-col gap-1'>
        <div className='flex flex-row items-center gap-2'>
          <User className='h-4 w-4' />
          {album.ownerFullName}
        </div>
        <div className='flex items-center gap-2 text-sm'>
          <Calendar className='h-4 w-4' />
          <span>
            {format(new Date(album.beginningDate), 'MMM d, yyyy')} - {format(new Date(album.endingDate), 'MMM d, yyyy')}
          </span>
        </div>
        <div className='text-sm'>
          Editor: {album.editor}
        </div>
      </CardContent>
    </Card>
  );
}
