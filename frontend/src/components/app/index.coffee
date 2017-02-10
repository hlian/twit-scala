React = require 'react'
classNames = require './index.styl'
e = require('react-e/bind') classNames

Icon = require '../icon'
NavigationLink = require '../navigation_link'


class App extends React.Component


  render: ->
    e 'div',
      e 'h1', t 'home.title'
      e 'p', t 'home.seeReadme'
      e 'hr'
      e '.internationalizationBlurb',
        t 'home.internationalizedString'
        ' '
        e Icon, name: 'globe'
      e 'p',
        e NavigationLink, to: '/demo', t 'home.demoAppLink'



module.exports = App
