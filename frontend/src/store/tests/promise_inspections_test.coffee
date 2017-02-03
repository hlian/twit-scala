actionsBuilder = require '../actions/builder'
reducerBuilder = require '../reducer/builder'
storeBuilder = require '../builder'

describe 'store - promise inspections', ->
  beforeEach ->
    @actions = actionsBuilder {}
    @reducer = reducerBuilder {}

  describe 'initial state', ->
    beforeEach ->
      @store = storeBuilder {@reducer}

    it 'returns an empty object', ->
      expect(@store.getState().promiseInspections).to.eql {}

  describe 'fulfilled', ->
    beforeEach ->
      initialState = promiseInspections: 'todos/create': pending: true
      @store = storeBuilder {initialState, @reducer}
      @store.dispatch @actions.promiseInspections.fulfilled(key: 'todos/create', value: 1)

    it 'updates the status on the object', ->
      expect(@store.getState().promiseInspections).to.eql
        'todos/create':
          fulfilled: true
          value: 1

  describe 'pending', ->
    beforeEach ->
      @store = storeBuilder {@reducer}
      @store.dispatch @actions.promiseInspections.pending('todos/create')

    it 'adds the status to the object', ->
      expect(@store.getState().promiseInspections).to.eql
        'todos/create': pending: true

  describe 'rejected', ->
    beforeEach ->
      initialState = promiseInspections: 'todos/create': pending: true
      @error = new Error 'network error'
      @store = storeBuilder {initialState, @reducer}
      @store.dispatch @actions.promiseInspections.rejected({key: 'todos/create', reason: @error})

    it 'updates the status on the object', ->
      expect(@store.getState().promiseInspections).to.eql
        'todos/create': {
          @error
          rejected: true
        }

  describe 'reset', ->
    beforeEach ->
      initialState = promiseInspections: 'todos/create': error: new Error('network error')
      @store = storeBuilder {initialState, @reducer}
      @store.dispatch @actions.promiseInspections.reset('todos/create')

    it 'removes the status from the object', ->
      expect(@store.getState().promiseInspections).to.eql {}
