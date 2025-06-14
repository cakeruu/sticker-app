export default function NotFound () {
  return (
    <>
      <section className='text-center py-12 space-y-4'>
        <h1 className='text-4xl font-bold tracking-tight'>404 Not Found</h1>
        <p className='text-lg text-muted-foreground max-w-2xl mx-auto'>
          The page you are looking for does not exist. Please check the URL and try again.
        </p>
      </section>
    </>
  );
}
