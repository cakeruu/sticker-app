import { Forum } from '@/components/forum';
import { getAllMessagesFromForum, getForum, getMembersInForum, getUnreadDirectMessages, getUnreadMessagesFromForum, getAllChatsFromForum } from '@/lib/api';
import ErrorComponent from './error-client';

export default async function ForumContent ({ id }: { id: number }) {
  const { success: forumMessages, error: forumMessagesError } = await getAllMessagesFromForum(id);
  const { success: unreadForumMessages, error: unreadForumMessagesError } = await getUnreadMessagesFromForum(id);
  const { success: unreadDirectMessages, error: unreadDirectMessagesError } = await getUnreadDirectMessages(id);
  const { success: forum, error: forumError } = await getForum(id);
  const { success: membersInForum, error: membersInForumError } = await getMembersInForum(id);
  const { success: chats, error: chatsError } = await getAllChatsFromForum(id);

  if (forumMessagesError) {
    return <ErrorComponent error={forumMessagesError} />;
  }

  if (unreadForumMessagesError) {
    return <ErrorComponent error={unreadForumMessagesError} />;
  }

  if (unreadDirectMessagesError) {
    return <ErrorComponent error={unreadDirectMessagesError} />;
  }

  if (forumError) {
    return <ErrorComponent error={forumError} />;
  }

  if (membersInForumError) {
    return <ErrorComponent error={membersInForumError} />;
  }

  if (chatsError) {
    return <ErrorComponent error={chatsError} />;
  }

  return (
    <Forum
      forumMessages={forumMessages!}
      unreadForumCount={unreadForumMessages!.length}
      unreadMessages={unreadDirectMessages!}
      forumInformation={forum!}
      membersInForum={membersInForum!}
      chats={chats!}
    />
  );
}
