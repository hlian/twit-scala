{createAction} = require 'redux-actions'


namespace = 'promiseInspections'
actions = {}
['fulfilled', 'pending', 'rejected', 'reset'].forEach (key) ->
  actions[key] = createAction "#{namespace}/#{key}"


module.exports = actions
