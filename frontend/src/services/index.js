import _ from 'lodash';

import todoService from './todoService';

const services = {
  todoService,
};

export default _.mapValues(services, Service => new Service());
