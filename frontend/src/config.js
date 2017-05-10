const config = {
  env: webpack.env.NODE_ENV,
};

export const isDevelop = config.env === 'dev';

export default config;
