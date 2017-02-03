# implicit requires
require 'coffee-loader'
require 'coffee-script'
require 'css-loader'
require 'postcss-loader'
require 'pug-loader'
require 'pug'
require 'regenerator-loader'
require 'regenerator-runtime'
require 'regenerator'
require 'style-loader'
require 'stylus-loader'
require 'stylus'


{NODE_ENV} = process.env
HtmlWebpackPlugin = require 'html-webpack-plugin'
path = require 'path'


cssLoaderQuery = ['modules']
cssLoaderQuery.push 'localIdentName=[folder]---[local]---[hash:base64]' unless NODE_ENV is 'production'


module.exports =

  devServer:
    inline: true
    historyApiFallback: true

  entry: './src/index.coffee'

  module: loaders: [
    loaders: ['style', "css?#{cssLoaderQuery.join '&'}", 'postcss', 'stylus']
    test: /\.styl$/
  ,
    loaders: ['regenerator', 'coffee']
    test: /\.coffee$/
  ,
    loader: 'pug'
    test: /\.pug$/
  ]

  output:
    path: 'dist'
    publicPath: '/'
    pathinfo: yes
    filename: '[hash].js'

  plugins: [
    # Output index.html from pug template
    new HtmlWebpackPlugin template: 'src/index.pug'
  ]

  resolve: extensions: ['', '.js', '.coffee']

  stylus:
    use: [(s) -> s.include path.join(__dirname, 'src')]
