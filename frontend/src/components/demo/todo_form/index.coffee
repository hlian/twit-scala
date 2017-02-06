{PropTypes} = React = require 'react'
classNames = require './index.styl'
e = require('react-e/bind')(classNames)


class TodoForm extends React.Component

  @propTypes:
    onSubmit: PropTypes.func.isRequired


  onSubmit: (event) =>
    event.preventDefault()
    return unless @input.value.trim()
    @props.onSubmit @input.value


  render: ->
    e '.root',
      e '.title', 'New Todo'
      e 'form', {@onSubmit},
        e 'input', ref: (node) => @input = node
        e 'button', type: 'submit', 'Add Todo'


module.exports = TodoForm
