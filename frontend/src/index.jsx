import React from 'react';
import { render } from 'react-dom';
import { Provider } from 'react-redux';
import { IndexRoute, Router, Route, browserHistory } from 'react-router';

import initStore from 'app/store';

import AppComponent from 'app/pages/app';
import DetailsComponent from 'app/pages/details';
import { TodoContainer } from 'app/pages/demo';
import NotFoundComponent from 'app/pages/notFound';

import 'app/assets/css/reset.css';
import 'app/assets/css/index.css';

const Main = () =>
  <Provider store={initStore()}>
    <Router history={browserHistory}>
      <Route path="/" component={AppComponent}>
        <IndexRoute component={DetailsComponent} />
        <Route path="demo" component={TodoContainer} />
        <Route path="*" component={NotFoundComponent} />
      </Route>
    </Router>
  </Provider>;

render(Main(), document.getElementById('app'));
