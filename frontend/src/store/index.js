import { applyMiddleware, createStore } from 'redux';
import reduxThunkMiddleware from 'redux-thunk';
import reduxLogger from 'redux-logger';

import { isDevelop } from 'app/config';
import { reducers } from './partitions';
import initState from './initState';

let middleware = [reduxThunkMiddleware];

if (isDevelop) {
  middleware = middleware.concat(reduxLogger({ collapsed: true, timestamp: false }));
}

export const buildStore = ({ reducer, middlewares = middleware }) =>
  createStore(reducer, applyMiddleware(...middlewares));

export default function initStore() {
  const store = buildStore({ reducer: reducers, middleware });
  initState(store.dispatch);
  return store;
}
