import { applyMiddleware, createStore } from 'redux';
import reduxThunkMiddleware from 'redux-thunk';
import reduxLogger from 'redux-logger';
import { routerMiddleware } from 'react-router-redux';

import reducers from './reducers';
import initState from './initState';

let middleware = [reduxThunkMiddleware];

if (process.env.NODE_ENV === 'local') {
  middleware = middleware.concat(reduxLogger({ collapsed: true, timestamp: false }));
}

export const buildStore = ({ reducer, middlewares = middleware }) =>
  createStore(reducer, applyMiddleware(...middlewares));

export default function initStore(browserHistory) {
  if (browserHistory) {
    middleware = middleware.concat(routerMiddleware(browserHistory));
  }
  const store = buildStore({ reducer: reducers, middleware });
  initState(store.dispatch);
  return store;
}
