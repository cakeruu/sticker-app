import Navbar from '@/components/navbar';
import { ScrollArea } from '@/components/ui/scroll-area';

export default function UnprotectedLayout ({
  children
}: {
    children: React.ReactNode;
}) {
  return (
    <>
      <Navbar />
      <ScrollArea>
        <div className='container mx-auto px-4 h-16 flex items-center justify-between' />
        <main className='h-[93dvh] w-dvw p-5'>
          {children}
        </main>
      </ScrollArea>
    </>
  );
}
