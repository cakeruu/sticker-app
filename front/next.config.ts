import type { NextConfig } from 'next';

const nextConfig: NextConfig = {
  // async headers () {
  //   return [
  //     {
  //       source: '/:path*',
  //       headers: [
  //         {
  //           key: 'Access-Control-Allow-Credentials',
  //           value: 'true'
  //         },
  //         {
  //           key: 'Access-Control-Allow-Origin',
  //           value: 'nlbt2r6r-8080.uks1.devtunnels.ms' // Your API domain
  //         }
  //       ]
  //     }
  //   ];
  // },
  // experimental: {
  //   serverActions: {
  //     // edit: updated to new key. Was previously `allowedForwardedHosts`
  //     allowedOrigins: [
  //       'localhost:3000',
  //       'nlbt2r6r-3000.uks1.devtunnels.ms',
  //       'nlbt2r6r-3000.uks1.devtunnels.ms:3000'
  //     ]
  //   }
  // },
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: '*',
        port: '',
        pathname: '/**'
      }
    ]
  }
};

export default nextConfig;
