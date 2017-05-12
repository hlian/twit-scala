const config = {
  env: process.env.NODE_ENV,
};

export const isDevelop = config.env === 'dev';

export default config;
