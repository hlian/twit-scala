mapping =

  recieveCreated: (state, {payload: createdTodo}) ->
    [state..., createdTodo]


  recieveUpdated: (state, {payload: updatedTodo}) ->
    state.map (todo) ->
      return updatedTodo if todo.id is updatedTodo.id
      todo


  set: (state, {payload: todos}) ->
    todos


module.exports = {initialState: [], mapping, namespace: 'todos'}
