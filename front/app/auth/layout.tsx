import { Card } from '@/components/ui/card';

export default function ProtectedLayout ({ children }: {children: React.ReactNode}) {
  return (
    <div className='min-h-dvh flex items-center justify-center p-4 relative overflow-hidden'>
      <div className='absolute bottom-0 left-0 w-full h-64 flex items-end' />

      <Card className='w-full max-w-md relative border bg-card/80 backdrop-blur-sm'>
        {
            children
        }
      </Card>
    </div>
  );
}
