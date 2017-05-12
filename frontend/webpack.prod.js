const merge = require('webpack-merge');
const HtmlWebpackPlugin = require('html-webpack-plugin')
const webpack = require('webpack');

const { baseConfig, paths } = require('./webpack.base');

module.exports = merge(baseConfig, {
  devtool: 'cheap-module-source-map',
  output: {
    publicPath: '/client/'
  },
  plugins: [
    new webpack.LoaderOptionsPlugin({
      minimize: true,
      debug: false
    }),
    new webpack.optimize.UglifyJsPlugin({
      beautify: false,
      comments: false,
      compress: {
        warnings: false
      }
    })
  ]
});

