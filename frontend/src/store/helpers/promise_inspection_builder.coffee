{coroutine} = require 'bluebird'
{fulfilled, pending, rejected} = require '../actions/promise_inspections_actions'


promiseInspectionBuilder = (namespace) ->
  (key, promiseBuilder) ->
    key = "#{namespace}/#{key}"

    coroutine (dispatch, args...) ->
      dispatch pending key

      try
        value = yield promiseBuilder(dispatch, args...)
      catch e
        reason = e

      if reason
        dispatch rejected {key, reason}
        throw reason
      else
        dispatch fulfilled {key, value}
        value


module.exports = promiseInspectionBuilder
