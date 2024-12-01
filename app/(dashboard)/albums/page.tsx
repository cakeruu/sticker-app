'use server';
import AvailableAlbumsPage from '@/components/available-albums';
import ErrorComponent from '@/components/error-client';
import { getAvailableAlbums } from '@/lib/api';

export default async function AlbumsPage () {
  const { success: availableAlbums, error } = await getAvailableAlbums();
  if (error) {
    return <ErrorComponent error={error} />;
  }
  if (!availableAlbums) return null;
  return (
    <AvailableAlbumsPage availableAlbums={availableAlbums!} />
  );
}
