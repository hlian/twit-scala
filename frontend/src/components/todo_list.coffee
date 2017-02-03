{PropTypes} = React = require 'react'
e = require 'react-e'
Todo = require './todo'


todoPropType = PropTypes.shape(
  completed: PropTypes.bool.isRequired
  id: PropTypes.number.isRequired
  text: PropTypes.string.isRequired
).isRequired


class TodoList extends React.Component

  @propTypes: ->
    onTodoClick: PropTypes.func.isRequired
    todos: PropTypes.arrayOf(todoPropType).isRequired


  render: ->
    e 'ul',
      @props.todos.map (todo) =>
        e Todo,
          completed: todo.completed
          key: todo.id
          onClick: => @props.onTodoClick todo.id
          text: todo.text


module.exports = TodoList
