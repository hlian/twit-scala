import { combineReducers } from 'redux';
import { routerReducer } from 'react-router-redux';
import _ from 'lodash';

import todos from './todo';

const reducers = {
  todos,
};

export const buildReducers = (initialState = {}) => {
  const instantiatedReducers = _.mapValues(reducers, reducer => reducer(initialState));

  return combineReducers({
    ...instantiatedReducers,
    routing: routerReducer,
  });
};
export default buildReducers();
