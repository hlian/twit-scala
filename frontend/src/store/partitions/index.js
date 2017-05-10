import { combineReducers } from 'redux';
import _ from 'lodash';

import services from 'app/services';

import todos from './todo';

/**
 * This object sets the name of the various sections of the application store and actions.
 * Each partition consists of an object which contains both actions and a reducer for the actions.
 */
const partitions = {
  todos,
};

/**
 * buildReducers takes an initial state and then creates all the reducers with the initial state.
 * all the reducers are then combined to create a single global store.
 */
export const buildReducers = (initialState = {}) => {
  const reducers = _.mapValues(partitions, ({ reducer }) => reducer(initialState));
  return combineReducers(reducers);
};

/**
 * buildActions initializes all actions by passing in dependencies. this can be used in tests
 * to pass in stubbed dependencies using sinon.
 */
export const buildActions = (deps = {}) =>
  _.mapValues(partitions, ({ actions }) => actions(deps));

export const actions = buildActions({ ...services, localStorage: window.localStorage });

export const reducers = buildReducers();
