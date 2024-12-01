import { Suspense } from 'react';
import { ForumSkeleton } from '@/components/forum-skeleton';
import ForumContent from '@/components/forum-content';

export default async function ForumPage (props: { params: Promise<{ id: number }> }) {
  const params = await props.params;
  const { id } = params;
  return (
    <Suspense fallback={<ForumSkeleton />}>
      <ForumContent id={id} />
    </Suspense>
  );
}
