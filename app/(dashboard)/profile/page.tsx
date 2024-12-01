import UpdateProfileForm from '@/components/update-profile-form';
import { getMemberInfo } from '@/lib/api';

export default async function ProfilePage () {
  const { success: memberInfo } = await getMemberInfo();

  if (!memberInfo) return null;
  return (
    <div className='p-5 flex flex-col justify-center items-center '>
      <h1 className='text-3xl font-bold mb-8'>Profile</h1>
      <UpdateProfileForm userInfo={memberInfo!} />
    </div>
  );
}
