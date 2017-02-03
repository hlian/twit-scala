_ = require 'lodash'
{createAction} = require 'redux-actions'
promiseInspectionsActions = require './promise_inspections_actions'
promiseInspectionBuilder = require '../helpers/promise_inspection_builder'


configurations = [
  require './todos_actions'
]


module.exports = (services) ->
  actions = promiseInspections: promiseInspectionsActions
  configurations.forEach ({identityActionTypes, mapping, mappingBuilder, namespace}) ->
    identityActions = {}
    _.each identityActionTypes, (key) ->
      identityActions[key] = createAction "#{namespace}/#{key}"
    promiseInspection = promiseInspectionBuilder namespace
    dependencies = {identityActions, promiseInspection, services}
    mapping = mappingBuilder(dependencies) if mappingBuilder
    actions[namespace] = _.assign {}, mapping, identityActions
  actions
