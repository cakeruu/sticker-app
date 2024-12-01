import { ErrorResponse } from '@/lib/types';

export default function ErrorComponent ({ error }: { error: ErrorResponse }) {
  return (
    <div className='container mx-auto py-8'>
      <div className='bg-destructive/15 text-destructive px-4 py-2 rounded-md'>
        Failed to load albums: {error.message}
      </div>
    </div>
  );
}
