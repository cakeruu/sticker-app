'use client';

import { Button } from '@/components/ui/button';
import { Album, Menu, Moon, Sun, User } from 'lucide-react';
import { useTheme } from 'next-themes';
import Link from 'next/link';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger
} from '@/components/ui/dropdown-menu';
import { Sheet, SheetContent, SheetTrigger } from '@/components/ui/sheet';
import { useGetTokenFromLocalStorage } from '@/lib/hooks';
import SignOutButton from './signout-button';

export default function Navbar () {
  const token = useGetTokenFromLocalStorage();
  const userFullName = token?.user_full_name;
  const { setTheme } = useTheme();
  const linkStyle_tw = 'relative cursor-pointer after:content-[""] after:absolute after:h-[1px] after:left-0 after:bottom-0 after:w-0 after:bg-foreground after:transition-all after:duration-200 hover:after:w-full';
  return (
    <nav className='border-b fixed top-0 left-0 w-full z-50 bg-background/85 dark:bg-background/60 backdrop-blur-sm'>
      <div className='container mx-auto p-4 flex-none flex items-center justify-between'>
        <Link href='/' className='flex items-center gap-2'>
          <Album className='h-6 w-6' />
          <span className='font-bold'>StickerApp</span>
        </Link>

        {/* Desktop Menu */}
        <div className='hidden md:flex items-center gap-6'>
          <Link
            className={linkStyle_tw}
            href='/albums'
          >
            Albums
          </Link>
          <Link
            className={linkStyle_tw}
            href='/collections'
          >
            My Collections
          </Link>
          <Link
            className={linkStyle_tw}
            href='/albums/my-albums'
          >
            My Albums
          </Link>
          <Link
            className={linkStyle_tw}
            href='/forums/my-forums'
          >
            My forums
          </Link>
        </div>

        <div className='hidden md:flex items-center gap-4'>
          <p>{userFullName}</p>
          <Button size='icon' className='rounded-full'>
            <Link href='/profile'>
              <User />
            </Link>
          </Button>
          <DropdownMenu modal={false}>
            <DropdownMenuTrigger asChild>
              <Button className='rounded-full' variant='outline' size='icon'>
                <Sun className='h-[1.2rem] w-[1.2rem] rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0' />
                <Moon className='absolute h-[1.2rem] w-[1.2rem] rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100' />
                <span className='sr-only'>Toggle theme</span>
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align='center' className='fixed-dropdown'>
              <DropdownMenuItem onClick={() => setTheme('light')}>
                Light
              </DropdownMenuItem>
              <DropdownMenuItem onClick={() => setTheme('dark')}>
                Dark
              </DropdownMenuItem>
              <DropdownMenuItem onClick={() => setTheme('system')}>
                System
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
          <SignOutButton />
        </div>

        {/* Mobile Menu */}
        <Sheet>
          <SheetTrigger asChild className='md:hidden'>
            <Button variant='outline' size='icon'>
              <Menu className='h-6 w-6' />
            </Button>
          </SheetTrigger>
          <SheetContent aria-modal>
            <div className='flex flex-col gap-4 mt-8'>
              <Link
                className={linkStyle_tw}
                href='/albums'
              >
                Albums
              </Link>
              <Link
                className={linkStyle_tw}
                href='/collections'
              >
                My Collections
              </Link>
              <Link
                className={linkStyle_tw}
                href='/albums/my-albums'
              >
                My Albums
              </Link>
              <Link
                className={linkStyle_tw}
                href='/forums/my-forums'
              >
                My forums
              </Link>
              <hr className='my-4' />
              <Button onClick={() => setTheme('light')} variant='ghost' className='justify-start'>
                Light Mode
              </Button>
              <Button onClick={() => setTheme('dark')} variant='ghost' className='justify-start'>
                Dark Mode
              </Button>
              <SignOutButton />
            </div>
          </SheetContent>
        </Sheet>
      </div>
    </nav>
  );
}
