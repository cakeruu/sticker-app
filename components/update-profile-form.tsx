
'use client';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { cn } from '@/lib/utils';
import * as z from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { toast } from 'sonner';
import DatePickerInput from '@/components/date-picker';
import { DateValue } from 'react-aria-components';
import { MemberResponse } from '@/lib/types';
import { updateProfileAction } from './actions';
import { memberLogin } from '@/lib/api-client';

const formSchema = z.object({
  email: z.string().email().min(1, 'Email is required'),
  password: z.string().min(1, 'Password can\'t be empty'),
  name: z.string().min(2, 'First name must be at least 2 characters').min(1, 'First name is required'),
  secondName: z.string().min(2, 'Last name must be at least 2 characters').min(1, 'Last name is required'),
  dateOfBirth: z.custom<DateValue>().refine((value) => {
    if (!value) {
      return false;
    }
    return true;
  }, {
    message: 'Date of birth is required'
  })
});

export type FormDataUpdate = z.infer<typeof formSchema>;

export default function UpdateProfileForm ({ userInfo }: { userInfo: MemberResponse }) {
  const [emailAlreadyInUse, setEmailAlreadyInUse] = useState(false);
  const { register, handleSubmit, setValue, formState: { errors } } = useForm<FormDataUpdate>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: userInfo.email,
      password: '',
      name: userInfo.name,
      secondName: userInfo.secondName,
      dateOfBirth: userInfo.dateOfBirth.substring(0, 10)
    }
  });

  const onSubmit = async (data: FormDataUpdate) => {
    const { success, error } = await updateProfileAction(data, new Date(data.dateOfBirth.toString()).toISOString());
    const login = await memberLogin({
      email: data.email,
      password: data.password
    });
    if (success) {
      toast.success(success.message);
      localStorage.setItem('token', login.success!.token);
    } else {
      toast.error(error?.message);
      if (error?.message === 'Email already in use') {
        setEmailAlreadyInUse(true);
      }
    }

    window.location.href = '/';
  };

  return (
    <>
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
            defaultValue={userInfo.dateOfBirth}
          />
          {errors.dateOfBirth && (
            <p className='text-sm text-red-500 mt-1'>{errors.dateOfBirth.message}</p>
          )}
        </div>
        <Button type='submit' className='w-full bg-primary text-primary-foreground hover:bg-primary/90'>
          Continue
        </Button>
      </form>
    </>
  );
}
