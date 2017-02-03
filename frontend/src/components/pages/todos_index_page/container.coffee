{actions, build} = require '../../container_helpers'
TodosIndexPage = require './'


mapDispatchToProps = toggleTodo: actions.todos.toggle


mapStateToProps = (state) -> {todos: state.todos}


module.exports = build {
  component: TodosIndexPage
  mapDispatchToProps
  mapStateToProps
}
