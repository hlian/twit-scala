_ = require 'lodash'
{combineReducers} = require 'redux'
{handleActions} = require 'redux-actions'


configurations = [
  require './promise_inspections_reducer'
  require './todos_reducer'
]


module.exports = (services) ->
  reducerMap = {}
  configurations.forEach ({initialState, mapping, mappingBuilder, namespace}) ->
    mapping = mappingBuilder({services}) if mappingBuilder
    namespacedMapping = _.mapKeys mapping, (value, key) -> "#{namespace}/#{key}"
    reducerMap[namespace] = handleActions namespacedMapping, initialState
  combineReducers reducerMap
