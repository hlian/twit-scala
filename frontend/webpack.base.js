const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const webpack = require('webpack')

const NODE_ENV = process.env.NODE_ENV || 'dev'
const paths = {
  src: path.resolve(__dirname, 'src'),
  htmlTemplate: path.resolve(__dirname, 'src', 'index.pug'),
  out: path.resolve(__dirname, '..', 'backend', 'public', 'client'),
  i18n: path.resolve(__dirname, 'src', 'utils', 'i18n'),
  logger: path.resolve(__dirname, 'src', 'utils', 'logger'),
}

const env = {
  'NODE_ENV': JSON.stringify(NODE_ENV),
}

const baseConfig = {
  context: paths.src,
  entry: ['babel-polyfill', './index.jsx'],
  output: {
    path: paths.out,
    filename: '[hash].js'
  },
  resolve: {
    extensions: ['.js', '.jsx'],
    modules: ['node_modules'],
    alias: {
      i18n: paths.i18n,
      logger: paths.logger,
      app: paths.src
    }
  },
  module: {
    rules: [
      { test: /\.css/, use: ['style-loader', 'css-loader'] },
      { test: /\.jsx?$/, use: ['babel-loader'], exclude: /node_modules/ },
      { test: /\.pug$/, use: ['pug-loader'] },
      { test: /\.svg$/, use: ['babel-loader', 'react-svg-loader'] },
      { test: /\.(jpg|png|ico)$/, use: ['file-loader'] },
      { test: require.resolve('snapsvg'), use: ['imports-loader?this=>window,fix=>module.exports=0'] },
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({ template: paths.htmlTemplate }),
    new webpack.ProvidePlugin({ 'logger': 'logger' }),
    new webpack.DefinePlugin({ 'process.env': env }),
  ],
  externals: {
    'cheerio': 'window',
    'react/addons': 'react',
    'react/lib/ExecutionEnvironment': 'react',
    'react/lib/ReactContext': 'react',
  }
}

module.exports = { baseConfig, paths }
