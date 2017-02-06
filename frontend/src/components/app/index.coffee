_ = require 'lodash'
React = require 'react'
style = require './index.styl'
e = require('react-e/bind') style

NavigationLink = require '../navigation_link'


class App extends React.Component


  render: ->
    e 'div',
      'Hello, world!'
      e NavigationLink, to: '/demo', 'Link to the demo ToDo app'



module.exports = App
