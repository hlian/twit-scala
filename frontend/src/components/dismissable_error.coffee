e = require 'react-e'
React = require 'react'


class DismissableError extends React.Component

  render: ->
    e 'div',
      e 'span', "An error occured: #{@props.message}"
      e 'a', onClick: @props.onDismiss, 'X'

module.exports = DismissableError
