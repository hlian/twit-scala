const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const webpack = require('webpack')

const NODE_ENV = process.env.NODE_ENV || 'dev'
const PORT = process.env.PORT || 3000

const paths = {
  src: path.resolve(__dirname, 'src'),
  template: path.resolve(__dirname, 'src', 'index.pug'),
  out: path.resolve(__dirname, '..', 'backend', 'public', 'client'),
  globalStyles: path.resolve(__dirname, 'src', 'global_styles', 'index.styl'),
  eslint: path.resolve(__dirname, '.eslintrc.json'),
  i18n: path.resolve(__dirname, 'src', 'utils', 'i18n')
}

let devTool = 'cheap-module-source-map'
let cssLoaderQuery = ['modules']
let publicPath = '/client/'

if (NODE_ENV !== 'production') {
  devTool = 'cheap-module-eval-source-map'
  cssLoaderQuery.push('localIdentName=[folder]---[local]---[hash:base64]')
  publicPath = '/'
}

const env = {
  'NODE_ENV': JSON.stringify(NODE_ENV),
}

module.exports = {
  devtool: devTool,
  context: paths.src,
  entry: ['babel-polyfill', './index.jsx'],
  output: {
    path: paths.out,
    publicPath: publicPath,
    filename: '[hash].js'
  },
  devServer: {
    inline: true,
    port: PORT,
    historyApiFallback: true,
    proxy: {
      '/api' : 'http://localhost:9000'
    }
  },
  resolve: {
    extensions: ['.js', '.jsx'],
    modules: ['node_modules'],
    alias: {
      i18n: paths.i18n,
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
    new HtmlWebpackPlugin({ template: paths.template }),
    new webpack.DefinePlugin({ 'webpack.env': env }),
  ],
  externals: {
    'cheerio': 'window',
    'react/addons': 'react',
    'react/lib/ExecutionEnvironment': 'react',
    'react/lib/ReactContext': 'react',
  }
}
