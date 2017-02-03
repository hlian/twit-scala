{Link} = require 'react-router'
e = require 'react-e'
React = require 'react'


class NavigationLink extends React.Component

  render: ->
    e Link, Object.assign(activeClassName: 'active', @props)


module.exports = NavigationLink
