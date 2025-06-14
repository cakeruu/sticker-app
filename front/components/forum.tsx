'use client';

import { useState, useRef, useEffect } from 'react';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Badge } from '@/components/ui/badge';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { MessageCircle, Users, Send, PlusCircle } from 'lucide-react';
import { cn, getTokenFromLocalStorage } from '@/lib/utils';
import { toast } from 'sonner';
import { Client } from '@stomp/stompjs';
import {
  ForumMessageResponse,
  DirectMessageResponse,
  ForumResponse,
  SimpleMemberResponse,
  SendForumMessageResponse,
  SendDirectMessageResponse,
  ErrorResponse,
  ChatResponse
} from '@/lib/types';

import { format } from 'date-fns';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { sendForumMessageAction, sendDirectMessageAction } from '@/components/actions';
import { useGetTokenFromLocalStorage } from '@/lib/hooks';
import { Popover, PopoverTrigger, PopoverContent } from './ui/popover';

interface ForumProps {
  forumMessages: ForumMessageResponse[]
  unreadForumCount: number
  unreadMessages: DirectMessageResponse[]
  forumInformation: ForumResponse
  membersInForum: SimpleMemberResponse[]
  chats: ChatResponse[]
}

export function Forum ({
  forumMessages: initialForumMessages,
  unreadForumCount,
  unreadMessages,
  forumInformation,
  membersInForum,
  chats: initialChats
}: ForumProps) {
  const [activeChat, setActiveChat] = useState<'forum' | number>('forum');
  const [isNewChatOpen, setIsNewChatOpen] = useState(false);
  const [chats, setChats] = useState(initialChats);
  const [forumMessages, setForumMessages] = useState(initialForumMessages);
  const scrollAreaRef = useRef<HTMLDivElement>(null);
  const memberId = useGetTokenFromLocalStorage()?.id;

  const scrollToBottom = () => {
    if (scrollAreaRef.current) {
      const scrollContainer = scrollAreaRef.current.querySelector('[data-radix-scroll-area-viewport]');
      if (scrollContainer) {
        scrollContainer.scrollTop = scrollContainer.scrollHeight;
      }
    }
  };

  useEffect(() => {
    const client = new Client({
      brokerURL: 'ws://localhost:8080/ws/websocket',
      connectHeaders: {
        Authorization: `Bearer ${getTokenFromLocalStorage()}`
      },
      onWebSocketError: (frame) => console.log({ frame }),
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000
    });

    client.onConnect = () => {
      // Subscribe to forum messages
      client.subscribe(`/topic/forum/${forumInformation.id}`, (message) => {
        const newForumMessage = JSON.parse(message.body);
        setForumMessages((prev) => [...prev, newForumMessage]);
      });

      // Subscribe to direct messages for each chat
      chats.forEach((chat) => {
        client.subscribe(`/topic/direct/${chat.firstMemberId}/${chat.secondMemberId}`, (message) => {
          const newDirectMessage = JSON.parse(message.body);
          setChats((prevChats) => {
            const updatedChats = prevChats.map((c) => {
              if (
                (c.firstMemberId === chat.firstMemberId && c.secondMemberId === chat.secondMemberId) ||
                (c.firstMemberId === chat.secondMemberId && c.secondMemberId === chat.firstMemberId)
              ) {
                return {
                  ...c,
                  messages: [...c.messages, newDirectMessage]
                };
              }
              return c;
            });
            return updatedChats;
          });
        });
      });
    };

    client.activate();
    return () => {
      client.deactivate();
    };
  }, [forumInformation.id, chats]);

  useEffect(() => {
    scrollToBottom();
  }, [chats, forumMessages]);

  const handleSendMessage = async (formData: FormData) => {
    const messageInput = formData.get('message') as string;
    if (!messageInput.trim()) return;
    let response: {
      error?: ErrorResponse;
      success?: SendForumMessageResponse | SendDirectMessageResponse | undefined;
    };

    if (activeChat === 'forum') {
      response = await sendForumMessageAction(forumInformation.id, messageInput as string);
    } else {
      response = await sendDirectMessageAction(forumInformation.id, activeChat, messageInput as string);
    }

    if (response.error) {
      toast.error(response.error.message);
    } else {
      toast.success(response.success?.message);

      const inputElement = document.querySelector('input[name="message"]') as HTMLInputElement;
      if (inputElement) {
        inputElement.value = '';
      }
    }
  };

  const formatMessageDate = (dateString: string) => {
    const date = new Date(dateString);
    if (isNaN(date.getTime())) {
      return '';
    }

    const now = new Date();
    const isToday = date.toDateString() === now.toDateString();
    const isYesterday = new Date(now.setDate(now.getDate() - 1)).toDateString() === date.toDateString();

    if (isToday) {
      return format(date, 'H:mm');
    } else if (isYesterday) {
      return 'Yesterday';
    } else {
      return format(date, 'dd/MM/yyyy');
    }
  };

  const getUnreadCount = (otherUserId: number) => {
    return unreadMessages.filter(msg => msg.senderId === otherUserId && !msg.isRead).length;
  };

  const getChatName = (chat: ChatResponse) => {
    if (memberId === chat.secondMemberId) {
      return chat.firstMemberName;
    }
    return chat.secondMemberFullName;
  };

  const getOtherUserId = (chat: ChatResponse) => {
    if (memberId === chat.secondMemberId) {
      return chat.firstMemberId;
    }
    return chat.secondMemberId;
  };

  const activeMessages = activeChat === 'forum'
    ? forumMessages
    : chats.find(chat =>
      chat.firstMemberId === activeChat || chat.secondMemberId === activeChat
    )?.messages || [];

  return (
    <div className='h-full flex flex-col'>
      <div className='flex flex-col h-full border rounded-lg overflow-hidden'>
        {/* Forum Information */}
        <div className='border-b p-5 bg-background'>
          <h1 className='text-2xl font-bold'>{forumInformation.albumName}</h1>
          <p className='text-sm text-muted-foreground'>
            Created {format(new Date(forumInformation.createdAt), 'PPP')}
          </p>
        </div>

        <div className='flex flex-1 min-h-0'>
          {/* Sidebar */}
          <div className='w-80 border-r bg-muted/10 flex flex-col overflow-hidden'>
            <Tabs defaultValue='forum' className='flex flex-col flex-1'>
              <TabsList className='w-full border-b bg-transparent p-5'>
                <TabsTrigger
                  value='forum'
                  variant
                  className='font-semibold text-muted-foreground hover:text-primary data-[state=active]:border-primary data-[state=active]:text-primary'
                  onClick={() => setActiveChat('forum')}
                >
                  <Users className='mr-2 h-4 w-4' />
                  Forum Chat
                  {unreadForumCount > 0 && (
                    <Badge
                      variant='default'
                      className='ml-2 bg-primary text-primary-foreground'
                    >
                      {unreadForumCount}
                    </Badge>
                  )}
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
                <ScrollArea className='h-full'>
                  <div className='p-4'>
                    <div className='space-y-2'>
                      <p className='text-sm text-muted-foreground'>
                        {forumMessages?.length} messages in forum
                      </p>
                    </div>
                  </div>
                </ScrollArea>
              </TabsContent>

              <TabsContent value='direct' className='flex-1'>
                <ScrollArea className='h-full'>
                  <div className='p-2 space-y-2'>
                    <Popover open={isNewChatOpen} onOpenChange={setIsNewChatOpen}>
                      <PopoverTrigger asChild className='w-full'>
                        <Button variant='outline'>
                          <PlusCircle className='mr-2 h-4 w-4' />
                          New Chat
                        </Button>
                      </PopoverTrigger>
                      <PopoverContent className='p-1'>
                        <div className='bg-background'>
                          {membersInForum.map((member) => {
                            if (member.id === parseInt(memberId + '')) {
                              return null;
                            }
                            return (
                              <button
                                key={member.id}
                                className='w-full p-1 text-left hover:bg-accent rounded'
                                onClick={() => {
                                  setActiveChat(member.id);
                                  setIsNewChatOpen(false);
                                }}
                              >
                                {member.fullName}
                              </button>
                            );
                          })}
                        </div>
                      </PopoverContent>
                    </Popover>

                    {chats.map((chat, index) => {
                      const lastMessage = chat.messages[chat.messages.length - 1];
                      const chatName = getChatName(chat);
                      const otherUserId = getOtherUserId(chat);
                      const unreadCount = getUnreadCount(
                        memberId === chat.firstMemberId ? chat.secondMemberId : chat.firstMemberId
                      );
                      return (
                        <button
                          key={`${chat.firstMemberId}-${chat.secondMemberId}-${index}`}
                          className={cn(
                            'w-full flex items-center justify-between p-2 rounded-lg hover:bg-accent text-left transition-colors duration-200',
                            (activeChat === chat.firstMemberId || activeChat === chat.secondMemberId) && 'bg-accent'
                          )}
                          onClick={() => setActiveChat(otherUserId)}
                        >
                          <div className='w-[80%]'>
                            <p className='font-medium text-[0.90rem]'>{chatName}</p>
                            <p className='text-[0.75rem] text-muted-foreground truncate overflow-hidden text-ellipsis'>
                              {lastMessage ? `${lastMessage.senderId === parseInt(memberId + '') ? 'You: ' : ''}${lastMessage.content}` : 'No messages yet'}
                            </p>
                          </div>
                          {unreadCount > 0 && (
                            <Badge
                              variant='default'
                              className='bg-primary p-0 px-3 text-primary-foreground text-[0.50rem] hover:bg-primary'
                            >
                              {unreadCount}
                            </Badge>
                          )}
                        </button>
                      );
                    })}
                  </div>
                </ScrollArea>
              </TabsContent>
            </Tabs>
          </div>

          {/* Main content area */}
          <div className='flex-1 flex flex-col w-[60%]'>
            <ScrollArea className='flex-1' ref={scrollAreaRef}>
              <div className='p-4 space-y-4'>
                {activeMessages.map((message, index, array) => {
                  const messageDate = message.sentAt;
                  const prevMessageDate = index > 0 ? array[index - 1].sentAt : null;

                  return (
                    <div key={message.id}>
                      {index === 0 || formatMessageDate(messageDate) !== formatMessageDate(prevMessageDate!)
                        ? (
                          <div className='text-center text-sm text-muted-foreground my-2'>
                            {formatMessageDate(messageDate)}
                          </div>
                          )
                        : null}
                      <div
                        className={cn(
                          'max-w-[70%] p-2 rounded-lg w-max',
                          message.senderId === parseInt(memberId + '')
                            ? 'bg-primary text-primary-foreground ml-auto'
                            : 'bg-muted'
                        )}
                      >
                        {activeChat === 'forum' &&
                          (
                            <p className='text-xs text-muted-foreground'>{(message as ForumMessageResponse).senderFullName}</p>
                          )}
                        <p className='text-sm'>{message.content}</p>
                        <p className={cn('text-[0.68rem] text-muted-foreground text-end',
                          message.senderId === parseInt(memberId + '') && 'text-primary-foreground'
                        )}
                        >
                          {messageDate ? format(new Date(messageDate), 'H:m') : 'Date not available'}
                        </p>
                      </div>
                    </div>
                  );
                })}
              </div>
            </ScrollArea>
            <div className='p-4 border-t'>
              <form action={handleSendMessage} className='flex items-center space-x-2'>
                <Input
                  autoComplete='off'
                  type='text'
                  name='message'
                  placeholder='Type your message...'
                  className='flex-1'
                />
                <Button type='submit'>
                  <Send className='h-4 w-4' />
                </Button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
