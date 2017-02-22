import { handleActions } from 'redux-actions';

const todoReducer = ({ todos = [] }) =>
  handleActions({
    TOGGLE_COMPLETE: ((state, action) =>
      state.map((todo) => {
        if (todo.id === action.payload) {
          todo.completed = !todo.completed;
        }
        return todo;
      })
    ),
    CREATE: (state, action) =>
      [...state, action.payload],
  }, todos);

export default todoReducer;
