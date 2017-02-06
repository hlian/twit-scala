classNames = require './index.styl'
e = require('react-e/bind')(classNames)
Link = require '../../link'
React = require 'react'
TodoList = require '../todo_list'


class FilteredTodoList extends React.Component

  @filters: [
    fn: -> true
    name: 'All'
  ,
    fn: (todo) -> not todo.completed
    name: 'Active'
  ,
    fn: (todo) -> todo.completed
    name: 'Completed'
  ]


  @propTypes: require('../todo_list').propTypes


  constructor: (props) ->
    super props
    @state = filter: FilteredTodoList.filters[1]


  getFilterLink: (filter) =>
    linkProps =
      active: filter is @state.filter
      key: filter.name
      onClick: => @setState {filter}
    e Link, linkProps, filter.name


  render: ->
    filteredTodos = @props.todos.filter @state.filter.fn
    e 'div',
      e '.links',
        FilteredTodoList.filters.map @getFilterLink
      if filteredTodos.length > 0
        e TodoList,
          onTodoClick: @props.onTodoClick
          todos: filteredTodos
      else
        e '.empty', 'None'


module.exports = FilteredTodoList
