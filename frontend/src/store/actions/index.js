import _ from 'lodash';

import services from 'app/services';
import todos from './todoActions';

const actions = {
  todos,
};

export const buildActions = deps => _.mapValues(actions, action => action(deps));
export default buildActions(services);
