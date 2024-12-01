import Collection from '@/components/collection';
import ErrorComponent from '@/components/error-client';
import { checkStickersInCollection } from '@/lib/api';

export default async function CollectionByIdPage (props: { params: Promise<{ id: number }> }) {
  const params = await props.params;
  const { id } = params;
  const { success: collection, error } = await checkStickersInCollection(id);
  if (error) {
    return <ErrorComponent error={error} />;
  }
  return (
    <Collection collection={collection!} />
  );
}
