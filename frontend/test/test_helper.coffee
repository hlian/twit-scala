process.env.NODE_ENV = 'test'

chai = require 'chai'
Promise = require 'bluebird'
require './mock_webpack_loaders'
sinon = require 'sinon'
sinonAsPromised = require 'sinon-as-promised'
sinonChai = require 'sinon-chai'


chai.use sinonChai
sinonAsPromised Promise


global.expect = chai.expect
global.sinon = sinon
