e = require 'react-e'
React = require 'react'
TodoForm = require '../../todo_form'


class TodosNewPage extends React.Component

  render: ->
    e TodoForm, onSubmit: @props.createTodo


module.exports = TodosNewPage
