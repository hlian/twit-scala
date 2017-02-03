actionsBuilder = require '../actions/builder'
reducerBuilder = require '../reducer/builder'
storeBuilder = require '../builder'

describe 'store - todos', ->
  beforeEach ->
    @todosService = {}
    @services = {@todosService}
    @actions = actionsBuilder @services
    @reducer = reducerBuilder @services

  describe 'initial state', ->
    beforeEach ->
      @store = storeBuilder {@reducer}

    it 'returns an empty array', ->
      expect(@store.getState().todos).to.eql []

  describe 'creating a todo', ->
    beforeEach ->
      @store = storeBuilder {@reducer}
      @todosService.create = sinon.stub()
        .withArgs 'write tests'
        .resolves {completed: false, id: 1, text: 'write tests'}
      @store.dispatch @actions.todos.create('write tests')

    it 'adds the todo to the list', ->
      expect(@store.getState().todos).to.eql [
        {completed: false, id: 1, text: 'write tests'}
      ]

  describe 'toggling a todo', ->
    beforeEach ->
      initialState =
        todos: [
          {completed: false, id: 1, text: 'write tests'}
          {completed: false, id: 2, text: 'write code'}
          {completed: false, id: 3, text: 'release'}
        ]
      @store = storeBuilder {initialState, @reducer}
      @todosService.toggle = sinon.stub()
        .withArgs 2
        .resolves {completed: true, id: 2, text: 'write code'}
      @store.dispatch @actions.todos.toggle(2)

    it 'toggles the todo in the list', ->
      expect(@store.getState().todos).to.eql [
        {completed: false, id: 1, text: 'write tests'}
        {completed: true, id: 2, text: 'write code'}
        {completed: false, id: 3, text: 'release'}
      ]
