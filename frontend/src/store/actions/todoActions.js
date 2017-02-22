import { createActions } from 'redux-actions';

const actions = (services) => {
  const { todoService } = services;

  return createActions({
    TOGGLE_COMPLETE: todo => todo.id,
    CREATE: text => todoService.create(text),
  });
};

export default actions;
