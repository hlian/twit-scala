{actions, build} = require '../../../container_helpers'
TodosNewPage = require './'


mapDispatchToProps = createTodo: actions.todos.create


module.exports = build {
  component: TodosNewPage
  mapDispatchToProps
}
