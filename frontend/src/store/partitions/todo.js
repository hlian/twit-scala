import { handleActions, createActions } from 'redux-actions';
import { identity } from 'app/utils';

const defaultInitialState = {
  creatingTodo: false,
  todos: [],
};

/**
 * Actions are composed of simpleActions and asyncActions
 * simpleActions are logicless events in which reducers may or may not subscribe to
 * asyncActions are actions that contain logic and should trigger simpleActions to invoke the reducer
 */
const actions = ({ todoService }) => {
  const simpleActions = createActions({
    toggleComplete: todo => todo.id,
    startCreateTodo: identity,
    completeCreateTodo: identity,
  });

  const asyncActions = {
    create: (text => (dispatch) => {
      dispatch(simpleActions.startCreateTodo());
      dispatch(simpleActions.completeCreateTodo(todoService.create(text)));
    }),
  };

  return { ...simpleActions, ...asyncActions };
};

/**
 * the reducer of a partition is a single function that responds to actions by taking the data/payload
 * and updating the store. all partition's reducers are combined into a single reducer function within
 * store/partitions/index.js
 */
const reducer = ({ todos } = {}) =>
  handleActions({
    toggleComplete: ((state, action) => {
      const updatedTodos = state.todos.map((todo) => {
        if (todo.id === action.payload) {
          todo.completed = !todo.completed;
        }
        return todo;
      });
      return { ...state, updatedTodos };
    }),
    startCreateTodo: ((state) => {
      return { ...state, creatingTodo: true };
    }),
    completeCreateTodo: ((state, action) => {
      return { creatingTodo: true, todos: [...state.todos, action.payload] };
    }),
  }, { ...defaultInitialState, ...todos });

export default { actions, reducer };
