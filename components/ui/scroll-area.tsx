'use client';

import * as React from 'react';
import * as ScrollAreaPrimitive from '@radix-ui/react-scroll-area';
import { cn } from '@/lib/utils';

const ScrollArea = React.forwardRef<
  React.ElementRef<typeof ScrollAreaPrimitive.Root>,
  React.ComponentPropsWithoutRef<typeof ScrollAreaPrimitive.Root>
>(({ className, children, ...props }, ref) => {
  const [isScrollbarVisible, setIsScrollbarVisible] = React.useState(false);
  const scrollTimeoutRef = React.useRef<NodeJS.Timeout | null>(null);

  const handleScroll = React.useCallback(() => {
    setIsScrollbarVisible(true);
    if (scrollTimeoutRef.current) {
      clearTimeout(scrollTimeoutRef.current);
    }
    scrollTimeoutRef.current = setTimeout(() => {
      setIsScrollbarVisible(false);
    }, 1000);
  }, []);

  React.useEffect(() => {
    return () => {
      if (scrollTimeoutRef.current) {
        clearTimeout(scrollTimeoutRef.current);
      }
    };
  }, []);

  return (
    <ScrollAreaPrimitive.Root
      ref={ref}
      className={cn('relative overflow-hidden', className)}
      {...props}
    >
      <ScrollAreaPrimitive.Viewport
        className='h-full w-full rounded-[inherit]'
        onScroll={handleScroll}
      >
        {children}
      </ScrollAreaPrimitive.Viewport>
      <ScrollBar isVisible={isScrollbarVisible} />
      <ScrollAreaPrimitive.Corner />
    </ScrollAreaPrimitive.Root>
  );
});
ScrollArea.displayName = ScrollAreaPrimitive.Root.displayName;

const ScrollBar = React.forwardRef<
  React.ElementRef<typeof ScrollAreaPrimitive.ScrollAreaScrollbar>,
  React.ComponentPropsWithoutRef<typeof ScrollAreaPrimitive.ScrollAreaScrollbar> & { isVisible: boolean }
>(({ className, orientation = 'vertical', isVisible, ...props }, ref) => (
  <ScrollAreaPrimitive.ScrollAreaScrollbar
    ref={ref}
    orientation={orientation}
    className={cn(
      'flex touch-none select-none transition-opacity duration-300 ease-in-out',
      orientation === 'vertical' &&
        'h-full w-2.5 border-l border-l-transparent p-[1px]',
      orientation === 'horizontal' &&
        'h-2.5 flex-col border-t border-t-transparent p-[1px]',
      isVisible ? 'opacity-100' : 'opacity-0',
      className
    )}
    {...props}
  >
    <ScrollAreaPrimitive.ScrollAreaThumb
      className='relative flex-1 rounded-full bg-border'
    />
  </ScrollAreaPrimitive.ScrollAreaScrollbar>
));
ScrollBar.displayName = ScrollAreaPrimitive.ScrollAreaScrollbar.displayName;

export { ScrollArea, ScrollBar };
