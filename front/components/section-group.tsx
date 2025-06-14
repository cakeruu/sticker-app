import { SectionStickerCollectedGroupResponse } from '@/lib/types';
import { StickerCard } from './sticker-card';
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger
} from '@/components/ui/accordion';
import { ScrollArea } from '@/components/ui/scroll-area';
import { flattenSection } from '@/lib/utils';

interface SectionGroupProps {
  section: SectionStickerCollectedGroupResponse;
  collectionId: number;
}

export function SectionGroup ({ section, collectionId }: SectionGroupProps) {
  const flattenedSection = flattenSection(section);
  const value = 'item' + section.sectionId;
  return (
    <Accordion type='single' defaultValue={value} collapsible className='w-full'>
      <AccordionItem value={value}>
        <AccordionTrigger className='text-xl font-semibold'>
          {section.sectionName}
        </AccordionTrigger>
        <AccordionContent>
          <ScrollArea className='w-full h-full'>
            <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 xl:grid-cols-5 gap-4 p-4'>
              {flattenedSection.stickers.map((sticker) => (
                <StickerCard key={sticker.id} sticker={sticker} collectionId={collectionId} />
              ))}
            </div>
          </ScrollArea>
        </AccordionContent>
      </AccordionItem>
    </Accordion>
  );
}
