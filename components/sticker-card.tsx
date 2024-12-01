
'use client';
import { Card, CardContent, CardHeader } from '@/components/ui/card';
import { Sticker } from '@/lib/utils';
import Image from 'next/image';
import { Badge } from '@/components/ui/badge';
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from './ui/tooltip';
import {
  ContextMenu,
  ContextMenuContent,
  ContextMenuItem,
  ContextMenuTrigger
} from '@/components/ui/context-menu';
import { collectStickerAction } from './actions';
import { toast } from 'sonner';

interface StickerCardProps {
  sticker: Sticker;
  collectionId: number;
}

export function StickerCard ({ sticker, collectionId }: StickerCardProps) {
  async function collectSticker () {
    const { success, error } = await collectStickerAction(collectionId, sticker.id);

    if (error) {
      toast.error(error.message);
    } else {
      toast.success(success?.message);
    }
  }
  return (
    <>
      {
        sticker.isCollected
          ? (
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
            )
          : (

            <ContextMenu>
              <ContextMenuTrigger>
                <TooltipProvider>
                  <Tooltip>
                    <TooltipTrigger asChild>
                      <Card className='w-[200px] h-fit opacity-50'>
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
                              className='object-cover rounded-md filter grayscale'
                            />
                          </div>
                          <p className='text-sm text-balance text-center text-muted-foreground line-clamp-2'>
                            This sticker is not collected yet
                          </p>
                        </CardContent>
                      </Card>
                    </TooltipTrigger>
                    <TooltipContent className='bg-muted text-muted-foreground'>
                      <p className='font-semibold'>Right click to collect it</p>
                    </TooltipContent>
                  </Tooltip>
                </TooltipProvider>
              </ContextMenuTrigger>
              <ContextMenuContent>
                <ContextMenuItem>
                  <button onClick={collectSticker}>
                    Collect Sticker
                  </button>
                </ContextMenuItem>
              </ContextMenuContent>
            </ContextMenu>
            )
    }
    </>
  );
}
