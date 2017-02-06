_ = require 'lodash'
{browserHistory} = require 'react-router'
DismissableError = require '../../dismissable_error'
Loading = require '../../loading'
NavigationLink = require '../../navigation_link'
React = require 'react'
style = require './index.styl'
e = require('react-e/bind') style


class TodoDemoApp extends React.Component


  componentDidMount: ->
    @props.initialize()


  componentWillReceiveProps: (nextProps) ->
    if nextProps.promiseInspections['todos/create']?.fulfilled
      browserHistory.push('/demo/todos')
      nextProps.resetPromiseInspection 'todos/create'


  isAsyncActionLoading: ->
    _.some @props.promiseInspections, 'pending'


  render: ->
    e 'div',
      e '.title', 'My Todo List'
      e '.links',
        e NavigationLink, to: '/demo/todos', 'Todos'
        e NavigationLink, to: '/demo/todos/new', 'New Todo'
      @props.children
      e Loading if @isAsyncActionLoading()
      for name, {error} of @props.promiseInspections when error
        e DismissableError,
          message: @props.error.stack or @props.error
          onDismiss: @props.resetPromiseInspection(name)


module.exports = TodoDemoApp
