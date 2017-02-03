{connect} = require 'react-redux'
actions = require '../store/actions'


build = ({component, mapDispatchToProps, mapStateToProps}) ->
  container = connect(mapStateToProps, mapDispatchToProps)(component)
  container.displayName = component.name + 'Container'
  container


module.exports = {actions, build}
