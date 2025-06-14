/* eslint-disable react/jsx-handler-names */
'use client';

import { useState } from 'react';
import { useForm, useFieldArray, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Calendar } from '@/components/ui/calendar';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { format } from 'date-fns';
import { CalendarIcon, Plus, Trash2 } from 'lucide-react';
import { cn } from '@/lib/utils';
import { createAlbum } from '@/lib/api';
import { toast } from 'sonner';
import { useRouter } from 'next/navigation';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import AutoGrowTextArea from '@/components/ui/auto-grow-text-area';

const stickerSchema = z.object({
  number: z.number().min(1, 'Sticker number must be at least 1'),
  name: z.string().min(1, 'Sticker name is required'),
  description: z.string(),
  image: z.string().min(1, 'Image URL is required')
});

const sectionSchema = z.object({
  name: z.string().min(1, 'Section name is required'),
  stickers: z.array(stickerSchema).min(1, 'At least one sticker is required')
});

const formSchema = z.object({
  name: z.string().min(1, 'Album name is required'),
  editor: z.string().min(1, 'Editor name is required'),
  beginningDate: z.date(),
  endingDate: z.date(),
  isPublic: z.boolean().default(false),
  sections: z.array(sectionSchema).min(1, 'At least one section is required')
});

type FormValues = z.infer<typeof formSchema>

type StickerWithId = z.infer<typeof stickerSchema> & { id: string }
type SectionWithIdStickers = Omit<z.infer<typeof sectionSchema>, 'stickers'> & { stickers: StickerWithId[] }

export default function CreateAlbum () {
  const router = useRouter();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const { register, control, handleSubmit, formState: { errors } } = useForm<FormValues & { sections: SectionWithIdStickers[] }>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      name: '',
      editor: '',
      beginningDate: new Date(),
      endingDate: new Date(),
      isPublic: true,
      sections: [{ name: '', stickers: [{ id: crypto.randomUUID(), number: 1, name: '', description: '', image: '' }] }]
    }
  });

  const { fields: sectionFields, append: appendSection, remove: removeSection } = useFieldArray({
    control,
    name: 'sections'
  });

  const onSubmit = async (data: FormValues & { sections: SectionWithIdStickers[] }) => {
    setIsSubmitting(true);
    // Remove the 'id' field from stickers before sending to the backend
    const dataToSend = {
      ...data,
      beginningDate: data.beginningDate.toISOString(),
      endingDate: data.endingDate.toISOString(),
      sections: data.sections.map(section => ({
        ...section,
        stickers: section.stickers.map((sticker) => sticker)
      }))
    };

    const { success, error } = await createAlbum(dataToSend);

    if (error) {
      toast.error(error.message);
    } else {
      toast.success(success?.message);
      router.push('/albums');
    }

    setIsSubmitting(false);
  };

  return (
    <div className='flex flex-col justify-center items-center p-5 w-full'>
      <div className='md:w-[57%]'>
        <h1 className='text-3xl font-bold mb-8'>Create New Album</h1>

        <form onSubmit={handleSubmit(onSubmit)} className='space-y-8'>
          <Card>
            <CardHeader>
              <CardTitle>Album Details</CardTitle>
            </CardHeader>
            <CardContent className='flex flex-col gap-4'>
              <div>
                <Input
                  placeholder='Album Name'
                  {...register('name')}
                />
                {errors.name && (
                  <p className='text-sm text-red-500 mt-1'>{errors.name.message}</p>
                )}
              </div>

              <div>
                <Input
                  placeholder='Editor'
                  {...register('editor')}
                />
                {errors.editor && (
                  <p className='text-sm text-red-500 mt-1'>{errors.editor.message}</p>
                )}
              </div>

              <div className='flex items-center gap-3'>
                <p>Public </p>
                <Input
                  className='size-[0.875rem]'
                  type='checkbox'
                  {...register('isPublic')}
                />
              </div>

              <div className='grid grid-cols-1 md:grid-cols-2 gap-4'>
                <div>
                  <Controller
                    control={control}
                    name='beginningDate'
                    render={({ field }) => (
                      <Popover>
                        <PopoverTrigger asChild>
                          <Button
                            variant='outline'
                            className={cn(
                              'w-full justify-start text-left font-normal',
                              !field.value && 'text-muted-foreground'
                            )}
                          >
                            <CalendarIcon className='mr-2 h-4 w-4' />
                            <span className='overflow-hidden'>
                              {field.value ? format(field.value, 'PPP') : 'Beginning Date'}
                            </span>
                          </Button>
                        </PopoverTrigger>
                        <PopoverContent className='w-auto p-0'>
                          <Calendar
                            mode='single'
                            selected={field.value}
                            onSelect={field.onChange}
                            initialFocus
                          />
                        </PopoverContent>
                      </Popover>
                    )}
                  />
                </div>
                <div>
                  <Controller
                    control={control}
                    name='endingDate'
                    render={({ field }) => (
                      <Popover>
                        <PopoverTrigger asChild>
                          <Button
                            variant='outline'
                            className={cn(
                              'w-full justify-start text-left font-normal',
                              !field.value && 'text-muted-foreground'
                            )}
                          >
                            <CalendarIcon className='mr-2 h-4 w-4' />
                            <span className='overflow-hidden'>
                              {field.value ? format(field.value, 'PPP') : 'Ending Date'}
                            </span>
                          </Button>
                        </PopoverTrigger>
                        <PopoverContent className='w-auto p-0'>
                          <Calendar
                            mode='single'
                            selected={field.value}
                            onSelect={field.onChange}
                            initialFocus
                          />
                        </PopoverContent>
                      </Popover>
                    )}
                  />
                </div>
              </div>
            </CardContent>
          </Card>

          {sectionFields.map((field, sectionIndex) => (
            <Card key={field.id}>
              <CardHeader className='flex flex-row items-center justify-between'>
                <CardTitle>Section {sectionIndex + 1}</CardTitle>
                <Button
                  type='button'
                  variant='ghost'
                  size='icon'
                  onClick={() => removeSection(sectionIndex)}
                >
                  <Trash2 className='h-4 w-4' />
                </Button>
              </CardHeader>
              <CardContent className='space-y-4'>
                <Input
                  placeholder='Section Name'
                  {...register(`sections.${sectionIndex}.name` as const)}
                />
                {errors.sections?.[sectionIndex]?.name && (
                  <p className='text-sm text-red-500 mt-1'>{errors.sections[sectionIndex].name.message}</p>
                )}

                <div className='space-y-4'>
                  <Controller
                    control={control}
                    name={`sections.${sectionIndex}.stickers`}
                    render={({ field }) => (
                      <>
                        {field.value.map((sticker) => (
                          <div key={sticker.id} className='relative grid gap-4 p-4 border rounded-lg'>
                            <Button
                              type='button'
                              variant='ghost'
                              size='icon'
                              className='absolute top-2 right-2'
                              onClick={() => {
                                const updatedStickers = field.value.filter((s) => s.id !== sticker.id);
                                field.onChange(updatedStickers);
                              }}
                            >
                              <Trash2 className='h-4 w-4' />
                            </Button>
                            <div className='grid grid-cols-2 gap-4 mt-10'>
                              <Input
                                placeholder='Sticker Number'
                                type='number'
                                {...register(`sections.${sectionIndex}.stickers.${field.value.findIndex(s => s.id === sticker.id)}.number` as const, { valueAsNumber: true })}
                              />
                              <Input
                                placeholder='Sticker Name'
                                {...register(`sections.${sectionIndex}.stickers.${field.value.findIndex(s => s.id === sticker.id)}.name` as const)}
                              />
                            </div>
                            <Input
                              placeholder='Image URL'
                              {...register(`sections.${sectionIndex}.stickers.${field.value.findIndex(s => s.id === sticker.id)}.image` as const)}
                            />
                            <AutoGrowTextArea
                              placeholder='Description'
                              {...register(`sections.${sectionIndex}.stickers.${field.value.findIndex(s => s.id === sticker.id)}.description` as const)}
                            />
                          </div>
                        ))}
                        <Button
                          type='button'
                          variant='outline'
                          onClick={() => {
                            field.onChange([...field.value, { id: crypto.randomUUID(), number: field.value.length + 1, name: '', description: '', image: '' }]);
                          }}
                          className='w-full'
                        >
                          <Plus className='w-4 h-4 mr-2' />
                          Add Sticker
                        </Button>
                      </>
                    )}
                  />
                  {errors.sections?.[sectionIndex]?.stickers && (
                    <p className='text-sm text-red-500 mt-1'>{errors.sections[sectionIndex].stickers.message}</p>
                  )}
                </div>
              </CardContent>
            </Card>
          ))}

          <Button
            type='button'
            variant='outline'
            onClick={() => appendSection({ name: '', stickers: [{ id: crypto.randomUUID(), number: 1, name: '', description: '', image: '' }] })}
            className='w-full'
          >
            <Plus className='w-4 h-4 mr-2' />
            Add Section
          </Button>

          <Button
            type='submit'
            className='w-full'
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Creating Album...' : 'Create Album'}
          </Button>
        </form>
      </div>
    </div>
  );
}
