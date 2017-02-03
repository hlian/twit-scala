{PropTypes} = React = require 'react'
e = require 'react-e'


class Link extends React.Component

  @propTypes:
    active: PropTypes.bool.isRequired
    onClick: PropTypes.func.isRequired


  render: ->
    if @props.active
      e 'span', @props.children
    else
      onClick = (event) =>
        event.preventDefault()
        @props.onClick()
      e 'a', {href: '#', onClick}, @props.children


module.exports = Link
