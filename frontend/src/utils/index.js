import _ from 'lodash';

export const identity = x => x;

export const renderIf = cond => body => { // eslint-disable-line arrow-parens
  if (cond) {
    return _.isFunction(body) ? body() : body;
  }
  return null;
};
