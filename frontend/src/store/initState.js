import { actions } from './partitions';

const initState = (dispatch) => {
  const todos = [
    'Delete Facebook',
    'Hit the gym',
    'Lawyer up',
  ];

  todos.forEach(todo => dispatch(actions.todos.create(todo)));
};

export default initState;
