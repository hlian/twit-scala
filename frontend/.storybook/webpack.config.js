const path = require('path')

const paths = {
  src: path.resolve(__dirname, '..', 'src'),
  i18n: path.resolve(__dirname, '..', 'src', 'utils', 'i18n')
}

module.exports = function(baseConfig, configType) {

  baseConfig.resolve.alias.i18n = paths.i18n;
  baseConfig.resolve.alias.app = paths.src;

  baseConfig.module.loaders.push([
    { test: /\.css/, loaders: ['style-loader', 'css-loader'] },
    { test: /\.pug$/, loaders: ['pug-loader'] },
    { test: /\.svg$/, loaders: ['babel-loader', 'react-svg-loader'] },
    { test: /\.(jpg|png)$/, loaders: ['file-loader'] },
    { test: require.resolve('snapsvg'), loaders: ['imports-loader?this=>window,fix=>module.exports=0'] },
  ]);

  return baseConfig;
}
