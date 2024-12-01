'use client';

import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { CardContent, CardHeader } from '@/components/ui/card';
import { Sticker } from 'lucide-react';
import { cn } from '@/lib/utils';
import Link from 'next/link';
import * as z from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';

import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { createMember } from '@/lib/api';
import { toast } from 'sonner';
import { useRouter } from 'next/navigation';
import DatePickerInput from '@/components/date-picker';
import { DateValue } from 'react-aria-components';

const formSchema = z.object({
  email: z.string().email(),
  password: z.string(),
  name: z.string().min(2),
  secondName: z.string().min(2),
  dateOfBirth: z.custom<DateValue>().nullable()
});

type FormData = z.infer<typeof formSchema>;

export default function RegisterPage () {
  const [emailAlreadyInUse, setEmailAlreadyInUse] = useState(false);
  const router = useRouter();
  const { register, handleSubmit, setValue, formState: { errors } } = useForm<FormData>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: '',
      password: '',
      name: '',
      secondName: '',
      dateOfBirth: null
    }
  });

  const onSubmit = async (data: FormData) => {
    const { success, error } = await createMember({
      email: data.email,
      password: data.password,
      name: data.name,
      secondName: data.secondName,
      dateOfBirth: data.dateOfBirth ? new Date(data.dateOfBirth.year, data.dateOfBirth.month - 1, data.dateOfBirth.day).toISOString() : '',
      role: 'PREMIUM'
    });

    if (success) {
      toast.success(success.message);
      router.push('/auth/login');
    } else {
      toast.error(error?.message);
      if (error?.message === 'Email already in use') {
        setEmailAlreadyInUse(true);
      }
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
              className={cn(
                'bg-background/50',
                emailAlreadyInUse && 'border-red-500 focus-visible:ring-red-500'
              )}
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
          <div className='space-y-2'>
            <label htmlFor='name' className='text-sm font-medium leading-none'>
              First Name
            </label>
            <Input
              id='name'
              type='text'
              placeholder='Enter your first name'
              className='bg-background/50'
              {...register('name', { required: true })}
            />
            {
              errors.name && <p className='text-sm text-red-500 mt-1'>{errors.name.message}</p>
            }
          </div>
          <div className='space-y-2'>
            <label htmlFor='secondName' className='text-sm font-medium leading-none'>
              Last Name
            </label>
            <Input
              id='secondName'
              type='text'
              placeholder='Enter your last name'
              className='bg-background/50'
              {...register('secondName', { required: true })}
            />
            {
              errors.secondName && <p className='text-sm text-red-500 mt-1'>{errors.secondName.message}</p>
            }
          </div>
          <div className='space-y-2'>
            <label className='text-sm font-medium leading-none'>
              Date of Birth
            </label>
            <DatePickerInput
              onChange={(newDate) => {
                if (newDate) {
                  setValue('dateOfBirth', newDate);
                }
              }}
            />
            {errors.dateOfBirth && (
              <p className='text-sm text-red-500 mt-1'>{errors.dateOfBirth.message}</p>
            )}
          </div>
          <div className='flex justify-end'>
            <Link
              href='/auth/login'
              className={cn(linkStyle_tw, 'text-sm text-muted-foreground')}
            >
              Login
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
