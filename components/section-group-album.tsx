import { Section2 } from '@/lib/types';
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger
} from '@/components/ui/accordion';
import { ScrollArea } from '@/components/ui/scroll-area';
import { StickerCardAlbum } from './sticker-card-album';

interface SectionGroupProps {
  section: Section2;
}

export function SectionGroupAlbum ({ section }: SectionGroupProps) {
  const value = 'item' + section.name;
  return (
    <Accordion type='single' defaultValue={value} collapsible className='w-full'>
      <AccordionItem value={value}>
        <AccordionTrigger className='text-xl font-semibold'>
          {section.name}
        </AccordionTrigger>
        <AccordionContent>
          <ScrollArea className='w-full h-full'>
            <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5 gap-4 p-4'>
              {
                section.stickers.map((sticker) => (
                  <StickerCardAlbum key={sticker.id} sticker={sticker} />
                ))
              }
            </div>
          </ScrollArea>
        </AccordionContent>
      </AccordionItem>
    </Accordion>
  );
}
