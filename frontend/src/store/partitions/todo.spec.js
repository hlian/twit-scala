import { buildStore } from 'app/store';
import { buildActions, buildReducers } from './';

describe('partitions - todos', () => {

  beforeEach(function() {
    this.todoService = {
      create: sinon.stub()
        .withArgs('write tests')
        .returns({ id: 1, todo: 'write tests', completed: false }),
    };
    this.services = { todoService: this.todoService };
    this.actions = buildActions(this.services);
    this.store = buildStore({ reducer: buildReducers() });
  });

  describe('initial state', () => {
    it('returns an empty list of todos', function() {
      expect(this.store.getState().todos.todos).to.eql([]);
    });
  });

  describe('creating todos', () => {
    beforeEach(function() {
      const action = this.actions.todos.create('write tests');
      this.store.dispatch(action);
    });

    it('adds the todo to the list', function() {
      expect(this.store.getState().todos.todos).to.eql([
        { id: 1, todo: 'write tests', completed: false },
      ]);
    });
  });

  describe('toggle todos', () => {
    beforeEach(function() {
      const toggleTodo = { id: 1, todo: 'write tests', completed: false };
      const action = this.actions.todos.toggleComplete(toggleTodo);
      const reducer = buildReducers({
        todos: {
          todos: [
            { id: 1, todo: 'write tests', completed: false },
            { id: 2, todo: 'write code', completed: false },
            { id: 3, todo: 'release', completed: false },
          ],
        },
      });
      this.store = buildStore({ reducer });
      this.store.dispatch(action);
    });

    it('toggles the todo in the list', function() {
      expect(this.store.getState().todos.todos).to.eql([
        { id: 1, todo: 'write tests', completed: true },
        { id: 2, todo: 'write code', completed: false },
        { id: 3, todo: 'release', completed: false },
      ]);
    });
  });

});
