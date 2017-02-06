_ = require 'lodash'
React = require 'react'
style = require './index.styl'
e = require('react-e/bind') style

NavigationLink = require '../navigation_link'


class App extends React.Component


  render: ->
    e 'div',
      e 'h1', t 'home.title'
      e 'p', t 'home.seeReadme'
      e 'hr'
      e 'p', t 'home.internationalizedString'
      e NavigationLink, to: '/demo', t 'home.demoAppLink'



module.exports = App
