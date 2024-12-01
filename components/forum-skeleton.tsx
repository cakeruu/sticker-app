import { Skeleton } from '@/components/ui/skeleton';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { MessageCircle, Users } from 'lucide-react';

export function ForumSkeleton () {
  return (
    <div className='h-full'>
      <div className='flex flex-col h-full border rounded-lg'>
        {/* Forum Information Skeleton */}
        <div className='border-b p-5 bg-background'>
          <Skeleton className='h-8 w-3/4 mb-2' />
          <Skeleton className='h-4 w-1/2' />
        </div>

        <div className='flex flex-1 min-h-0'>
          {/* Sidebar Skeleton */}
          <div className='w-80 border-r bg-muted/10 flex flex-col'>
            <Tabs defaultValue='direct' className='flex flex-col flex-1'>
              <TabsList className='w-full border-b bg-transparent p-5'>
                <TabsTrigger
                  value='forum'
                  variant
                  className='font-semibold text-muted-foreground hover:text-primary data-[state=active]:border-primary data-[state=active]:text-primary'
                >
                  <Users className='mr-2 h-4 w-4' />
                  Forum Chat
                </TabsTrigger>
                <TabsTrigger
                  value='direct'
                  variant
                  className='font-semibold text-muted-foreground hover:text-primary data-[state=active]:border-primary data-[state=active]:text-primary'
                >
                  <MessageCircle className='mr-2 h-4 w-4' />
                  Direct Messages
                </TabsTrigger>
              </TabsList>

              <TabsContent value='forum' className='flex-1'>
                <ScrollArea className='h-[400px]'>
                  <div className='p-4'>
                    <div className='space-y-2'>
                      <Skeleton className='h-4 w-3/4' />
                    </div>
                  </div>
                </ScrollArea>
              </TabsContent>

              <TabsContent value='direct' className='flex-1'>
                <ScrollArea className='h-[400px]'>
                  <div className='p-2 space-y-2'>
                    {[...Array(5)].map((_, index) => (
                      <div key={index} className='w-full flex items-center justify-between p-2 rounded-lg'>
                        <div className='space-y-2'>
                          <Skeleton className='h-4 w-24' />
                          <Skeleton className='h-3 w-32' />
                        </div>
                        <Skeleton className='h-5 w-5 rounded-full' />
                      </div>
                    ))}
                  </div>
                </ScrollArea>
              </TabsContent>
            </Tabs>
          </div>

          {/* Main content area Skeleton */}
          <div className='flex-1 p-6'>
            <div className='rounded-lg border h-full flex items-center justify-center'>
              <Skeleton className='h-6 w-48' />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
