import React from 'react';
import { render } from 'react-dom';
import { Provider } from 'react-redux';
import { IndexRoute, Router, Route, browserHistory } from 'react-router';
import { syncHistoryWithStore } from 'react-router-redux';

import AppComponent from 'app/pages/app';
import DetailsComponent from 'app/pages/details';
import { TodoContainer } from 'app/pages/demo';
import NotFoundComponent from 'app/pages/notFound';
import initStore from 'app/store';

import 'app/assets/css/reset.css';
import 'app/assets/css/index.css';

const store = initStore(browserHistory);
const history = syncHistoryWithStore(browserHistory, store);

const Main = () =>
  <Provider store={store}>
    <Router history={history}>
      <Route path="/" component={AppComponent}>
        <IndexRoute component={DetailsComponent} />
        <Route path="demo" component={TodoContainer} />
        <Route path="*" component={NotFoundComponent} />
      </Route>
    </Router>
  </Provider>;

render(Main(), document.getElementById('app'));
