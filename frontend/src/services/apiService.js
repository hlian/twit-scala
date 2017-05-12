import url from 'url';
import _ from 'lodash';

/**
 * ApiService contains all functions used to make requests to the backend. It also
 * contains helper functions that are used to help make web requests less verbose
 */
class ApiService {

  constructor() {
    this.baseUrl = 'api';
  }

  getTodos() {
    this.getJson('/todos');
  }

  getJson(endpoint, json = {}) {
    return this.requestJson('GET', endpoint, { query: json });
  }

  postJson(endpoint, json = {}) {
    return this.requestJson('POST', endpoint, { body: json });
  }

  putJson(endpoint, json = {}) {
    return this.requestJson('PUT', endpoint, { body: json });
  }

  requestJson(method, endpoint, { body = {}, query = {} }) {
    const pathname = this.baseUrl + endpoint;
    logger.group(`making ${method} request to ${pathname}`);
    logger.info('query: ', query);
    logger.info('body: ', body);
    return fetch(url.format({ pathname, query }), {
      method,
      credentials: 'same-origin',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `basic ${this.apiAuthentication}`,
      },
      body: (method === 'GET' || method === 'DELETE') ? null : JSON.stringify(body),
    }).then(this.handleResponse).catch((err) => {
      logger.error(`Request to ${endpoint} failed: ${err}`);
    }).then(() => logger.groupEnd());
  }

  handleResponse(response) { // eslint-disable-line class-methods-use-this
    return response.json()
      .catch((err) => {
        throw new Error(`failed to parse response body: ${err}`);
      }).then((json) => {
        if (!response.ok || json.status === 'error') {
          const error = new Error(json.message);
          error.response = response;
          throw error;
        }
        return json.data.length === 1 ? _.head(json.data) : json.data;
      });
  }
}

export default ApiService;
