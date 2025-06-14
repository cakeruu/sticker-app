
'use client';
import { Card, CardContent, CardHeader } from '@/components/ui/card';
import { Sticker } from '@/lib/types';
import Image from 'next/image';
import { Badge } from '@/components/ui/badge';

interface StickerCardProps {
  sticker: Sticker;
}

export function StickerCardAlbum ({ sticker }: StickerCardProps) {
  return (
    <Card className='w-[200px] h-fit transition-all hover:scale-105'>
      <CardHeader className='p-4'>
        <div className='flex justify-between items-center'>
          <Badge variant='secondary'>#{sticker.number}</Badge>
          <h3 className='font-semibold text-sm truncate'>{sticker.name}</h3>
        </div>
      </CardHeader>
      <CardContent className='p-4 pt-0'>
        <div className='relative w-full h-[200px] mb-2'>
          <Image
            src={sticker.image || '/placeholder-sticker.jpg'}
            alt={sticker.name}
            fill
            className='object-cover rounded-md'
          />
        </div>
        <p className='text-sm text-balance text-center text-muted-foreground line-clamp-2'>
          {sticker.description}
        </p>
      </CardContent>
    </Card>
  );
}
