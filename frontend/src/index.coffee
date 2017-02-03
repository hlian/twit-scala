require './index.styl'

{browserHistory} = require 'react-router'
{IndexRedirect, Route, Router} = require 'react-router'
{Provider} = require 'react-redux'
AppContainer = require './components/app/container'
e = require 'react-e'
React = require 'react'
ReactDOM = require 'react-dom'
store = require './store'
TodosIndexPageContainer = require './components/pages/todos_index_page/container'
TodosNewPageContainer = require './components/pages/todos_new_page/container'


Main = ->
  e Provider, {store},
    e Router, {history: browserHistory},
      e Route, component: AppContainer, path: '/' ,
        e IndexRedirect, to: '/todos'
        e Route, component: TodosIndexPageContainer, path: '/todos'
        e Route, component: TodosNewPageContainer, path: '/todos/new'


ReactDOM.render Main(), document.getElementById('app')


# Assign React to window for Chrome React Devevloper Tools
window.React = React
