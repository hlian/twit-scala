_ = require 'lodash'
React = require 'react'
style = require './index.styl'
e = require('react-e/bind') style

NavigationLink = require '../navigation_link'


class App extends React.Component


  render: ->
    e 'div',
      e 'h1', 'Scala+React Template App'
      e 'p', 'See README.md for developer documentation.'
      e 'hr'
      e NavigationLink, to: '/demo', 'Check out the demo app'



module.exports = App
