import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { getMemberForums } from '@/lib/api';
import { format } from 'date-fns';
import { MessageSquare } from 'lucide-react';
import Link from 'next/link';
import ErrorComponent from '@/components/error-client';

export default async function MyForumsPage () {
  const { success: forums, error } = await getMemberForums();

  if (error) {
    return <ErrorComponent error={error!} />;
  }

  if (!forums) return null;

  return (
    <div className='p-5'>
      <h1 className='text-3xl font-bold mb-8'>My Forums</h1>
      <div className='grid gap-6 md:grid-cols-2 lg:grid-cols-3'>
        {forums.map((forum) => (
          <Link key={forum.id} href={`/forums/${forum.id}`}>
            <Card className='hover:bg-accent/50 transition-colors'>
              <CardHeader>
                <CardTitle className='flex items-center gap-2'>
                  <MessageSquare className='h-5 w-5' />
                  {forum.albumName}
                </CardTitle>
                <CardDescription>
                  Created {format(new Date(forum.createdAt), 'PPP')}
                </CardDescription>
              </CardHeader>
              <CardContent>
                <p className='text-sm text-muted-foreground'>
                  Click to join the discussion
                </p>
              </CardContent>
            </Card>
          </Link>
        ))}
        {
          forums.length === 0 && (
            <Link href='/albums'>
              <Card className='hover:bg-accent/50 transition-colors'>
                <CardHeader>
                  <CardTitle className='text-center'>
                    You still haven't join any forum
                  </CardTitle>
                  <CardContent className='p-0'>
                    <p className='text-sm text-center text-muted-foreground'>
                      Click here to join a forum
                    </p>
                  </CardContent>
                </CardHeader>
              </Card>
            </Link>
          )
        }
      </div>
    </div>
  );
}
