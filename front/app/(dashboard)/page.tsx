import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { getToken } from '@/lib/server-utils';
import { Album, BookOpen, Users } from 'lucide-react';
import Link from 'next/link';
import Image from 'next/image';

export default async function Home () {
  const token = await getToken();
  return (
    <div className='flex flex-col h-full justify-center items-center gap-28 w-full'>
      <section className='text-center'>
        <h1 className='text-4xl font-bold tracking-tight flex items-center justify-center gap-2'>
          <p>Welcome back {token?.user_name}</p>
          <Image
            src='https://media.giphy.com/media/hvRJCLFzcasrR4ia7z/giphy.gif'
            width='45'
            height='45'
            alt='waving hand'
            className='-mt-2'
          />
        </h1>
        <p className='text-lg text-muted-foreground max-w-2xl mx-auto'>
          Create, collect, and trade stickers of your favorite albums<br />
          Join our community of collectors!
        </p>
      </section>
      <div className='grid md:grid-cols-3 gap-6'>
        <Card>
          <CardHeader>
            <CardTitle className='flex items-center gap-2'>
              <Album className='h-5 w-5' />
              Create Albums
            </CardTitle>
            <CardDescription>Design and publish your own sticker albums</CardDescription>
          </CardHeader>
          <CardContent>
            <Link href='/albums/create'>
              <Button className='w-full'>Start Creating</Button>
            </Link>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className='flex items-center gap-2'>
              <BookOpen className='h-5 w-5' />
              Add a new collection
            </CardTitle>
            <CardDescription>Start new collection and collect stickers from available albums</CardDescription>
          </CardHeader>
          <CardContent>
            <Link href='/albums'>
              <Button className='w-full'>View Albums</Button>
            </Link>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className='flex items-center gap-2'>
              <Users className='h-5 w-5' />
              Community
            </CardTitle>
            <CardDescription>Connect with other collectors and trade stickers</CardDescription>
          </CardHeader>
          <CardContent>
            <Link href='/albums'>
              <Button className='w-full'>Join Forums</Button>
            </Link>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
