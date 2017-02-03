{coroutine} = require 'bluebird'


identityActionTypes = [
  'recieveCreated'
  'recieveUpdated'
  'set'
]


mappingBuilder = ({identityActions, promiseInspection, services}) ->
  {recieveCreated, recieveUpdated, set} = identityActions
  {todosService} = services


  create: (text) ->
    promiseInspection 'create', coroutine (dispatch) ->
      createdTodo = yield todosService.create text
      dispatch recieveCreated(createdTodo)


  getAll: ->
    promiseInspection 'getAll', coroutine (dispatch) ->
      todos = yield todosService.getAll()
      dispatch set(todos)


  toggle: (id) ->
    promiseInspection 'toggle', coroutine (dispatch) ->
      updatedTodo = yield todosService.toggle(id)
      dispatch recieveUpdated(updatedTodo)


module.exports = {identityActionTypes, mappingBuilder, namespace: 'todos'}
