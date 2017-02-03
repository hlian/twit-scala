_ = require 'lodash'


mapping =

  fulfilled: (state, {payload: {key, value}}) ->
    _.assign {}, state, "#{key}": {fulfilled: true, value}


  pending: (state, {payload: key}) ->
    _.assign {}, state, "#{key}": pending: true


  rejected: (state, {payload: {key, reason}}) ->
    _.assign {}, state, "#{key}": {error: reason, rejected: true}


  reset: (state, {payload: key}) ->
    _.omit state, key


module.exports = {initialState: {}, mapping, namespace: 'promiseInspections'}
