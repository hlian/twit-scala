e = require 'react-e'
FilteredTodoList = require '../../filtered_todo_list'
React = require 'react'


class TodosIndexPage extends React.Component


  render: ->
    e 'div',
      e FilteredTodoList,
        onTodoClick: @props.toggleTodo
        todos: @props.todos


module.exports = TodosIndexPage
