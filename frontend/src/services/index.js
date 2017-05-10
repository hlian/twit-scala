import _ from 'lodash';

import config from 'app/config';

import todoService from './todoService';
import apiService from './apiService';

const services = {
  todoService,
  apiService,
};

export default _.mapValues(services, Service => new Service(config));
