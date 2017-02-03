_ = require 'lodash'
Promise = require 'bluebird'


class TodosService

  constructor: (dependencies) ->
    @nextId = 3
    @todos = [
      {completed: true, id: 1, text: 'write tests'}
      {completed: false, id: 2, text: 'write code'}
    ]


  create: (text) ->
    return Promise.reject('text cannot be empty') unless text
    id = @nextId
    @nextId += 1
    todo = {completed: false, id, text}
    @todos.push todo
    Promise.delay 1000, _.clone(todo)


  getAll: ->
    Promise.delay 1000, _.cloneDeep(@todos)


  toggle: (id) ->
    for todo in @todos when todo.id is id
      todo.completed = not todo.completed
      return Promise.delay 1000, _.clone(todo)
    Promise.reject 'Todo does not exist'


module.exports = TodosService
