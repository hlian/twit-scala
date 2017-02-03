{PropTypes} = React = require 'react'
e = require 'react-e'


class Todo extends React.Component

  @propTypes:
    completed: PropTypes.bool.isRequired
    onClick: PropTypes.func.isRequired
    text: PropTypes.string.isRequired


  render: ->
    textDecoration = if @props.completed then 'line-through' else 'none'
    e 'li', onClick: @props.onClick, style: {textDecoration}, @props.text


module.exports = Todo
