'use client';

import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { CardContent, CardHeader } from '@/components/ui/card';
import { Sticker } from 'lucide-react';
import { cn } from '@/lib/utils';
import Link from 'next/link';
import * as z from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';

import { useForm } from 'react-hook-form';
import { memberLogin } from '@/lib/api-client';
import { toast } from 'sonner';
import { useRouter } from 'next/navigation';

const formSchema = z.object({
  email: z.string().email(),
  password: z.string()
});

type FormData = z.infer<typeof formSchema>;

export default function LoginPage () {
  const router = useRouter();
  const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: '',
      password: ''
    }
  });

  const onSubmit = async (data: FormData) => {
    const { success, error } = await memberLogin({
      email: data.email,
      password: data.password
    });

    if (success) {
      toast.success(success.message);
      localStorage.setItem('token', success.token);
      router.push('/');
    } else {
      toast.error(error?.message);
    }
  };

  const linkStyle_tw = 'relative cursor-pointer after:content-[""] after:absolute after:h-[1px] after:left-0 after:bottom-0 after:w-0 after:bg-muted-foreground after:transition-all after:duration-200 hover:after:w-full';
  return (
    <>
      <CardHeader className='space-y-6 items-center text-center'>
        <div className='size-16 bg-primary rounded-full flex items-center justify-center text-primary-foreground'>
          <Sticker className='size-8' />
        </div>
        <div>
          <h2 className='text-2xl font-semibold tracking-tight text-card-foreground'>Welcome back</h2>
          <p className='text-sm text-muted-foreground'>Please login to your <strong className='italic'>StickerApp</strong> account</p>
        </div>
      </CardHeader>
      <CardContent className='space-y-4'>
        <form onSubmit={handleSubmit(onSubmit)} className='space-y-4'>
          <div className='space-y-2'>
            <label htmlFor='email' className='text-sm font-medium leading-none'>
              Email
            </label>
            <Input
              id='email'
              type='email'
              placeholder='Enter your email'
              className='bg-background/50'
              {...register('email', { required: true })}
            />
            {
              errors.email && <p className='text-sm text-red-500 mt-1'>{errors.email.message}</p>
            }
          </div>
          <div className='space-y-2'>
            <label htmlFor='password' className='text-sm font-medium leading-none'>
              Password
            </label>
            <Input
              id='password'
              type='password'
              placeholder='Enter your password'
              className='bg-background/50'
              {...register('password', { required: true })}
            />
            {
              errors.password && <p className='text-sm text-red-500 mt-1'>{errors.password.message}</p>
            }
          </div>
          <div className='flex justify-end'>
            <Link
              href='/auth/register'
              className={cn(linkStyle_tw, 'text-sm text-muted-foreground')}
            >
              Register
            </Link>
          </div>
          <Button type='submit' className='w-full bg-primary text-primary-foreground hover:bg-primary/90'>
            Continue
          </Button>
        </form>
      </CardContent>
    </>
  );
}
