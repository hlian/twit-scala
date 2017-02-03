{applyMiddleware, createStore} = require 'redux'
{default: reduxThunk} = require 'redux-thunk'


middlewares = [reduxThunk]
if process.env.NODE_ENV is 'local'
  reduxLogger = require 'redux-logger'
  middlewares.push reduxLogger(collapsed: true, timestamp: false)


module.exports = ({initialState = {}, reducer}) ->
  createStore reducer, initialState, applyMiddleware(middlewares...)
