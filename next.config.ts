import type { NextConfig } from 'next';

const nextConfig: NextConfig = {
  async headers () {
    return [
      {
        source: '/:path*',
        headers: [
          {
            key: 'Access-Control-Allow-Credentials',
            value: 'true'
          },
          {
            key: 'Access-Control-Allow-Origin',
            value: 'ec2-18-193-120-116.eu-central-1.compute.amazonaws.com' // Your API domain
          }
        ]
      }
    ];
  },
  experimental: {
    serverActions: {
      // edit: updated to new key. Was previously `allowedForwardedHosts`
      allowedOrigins: [
        'localhost:3000',
        'ec2-18-193-120-116.eu-central-1.compute.amazonaws.com',
        'ec2-18-193-120-116.eu-central-1.compute.amazonaws.com:443'
      ]
    }
  },
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
