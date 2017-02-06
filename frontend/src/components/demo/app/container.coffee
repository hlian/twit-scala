{actions, build} = require '../../container_helpers'
App = require './'


mapDispatchToProps =
  initialize: actions.todos.getAll
  resetPromiseInspection: actions.promiseInspections.reset


mapStateToProps = (state) ->
  {promiseInspections: state.promiseInspections}


module.exports = build {
  component: App
  mapDispatchToProps
  mapStateToProps
}
