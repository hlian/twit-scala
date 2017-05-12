const merge = require('webpack-merge');
const HtmlWebpackPlugin = require('html-webpack-plugin')

const { baseConfig, paths } = require('./webpack.base');

const PORT = process.env.PORT || 3000

module.exports = merge(baseConfig, {
  devtool: 'cheap-module-source-map',
  output: {
    publicPath: '/'
  },
  devServer: {
    inline: true,
    port: PORT,
    historyApiFallback: true,
    proxy: {
      '/api' : 'http://localhost:9000'
    },
  }
})

