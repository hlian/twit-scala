_ = require 'lodash'
{browserHistory} = require 'react-router'
classNames = require './index.styl'
DismissableError = require '../dismissable_error'
e = require('react-e/bind')(classNames)
Loading = require '../loading'
NavigationLink = require '../navigation_link'
React = require 'react'


class App extends React.Component


  componentDidMount: ->
    @props.initialize()


  componentWillReceiveProps: (nextProps) ->
    if nextProps.promiseInspections['todos/create']?.fulfilled
      browserHistory.push('/todos')
      nextProps.resetPromiseInspection 'todos/create'


  isAsyncActionLoading: ->
    _.some @props.promiseInspections, 'pending'


  render: ->
    e 'div',
      e '.title', 'My Todo List'
      e '.links',
        e NavigationLink, to: '/todos', 'Todos'
        e NavigationLink, to: '/todos/new', 'New Todo'
      @props.children
      e Loading if @isAsyncActionLoading()
      for name, {error} of @props.promiseInspections when error
        e DismissableError,
          message: @props.error.stack or @props.error
          onDismiss: @props.resetPromiseInspection(name)


module.exports = App
