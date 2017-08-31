import React from 'react';
import { render } from 'react-dom';
import { Provider } from 'react-redux';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import initStore from 'app/store';

import AppComponent from 'app/pages/app';
import DetailsComponent from 'app/pages/details';
import { TodoContainer } from 'app/pages/demo';
import NotFoundComponent from 'app/pages/notFound';

import 'app/assets/css/reset.css';
import 'app/assets/css/index.css';

const Main = () => (
  <Provider store={initStore()}>
    <AppComponent>
      <BrowserRouter>
        <Switch>
          <Route exact path="/" component={DetailsComponent} />
          <Route exact path="/demo" component={TodoContainer} />
          <Route path="*" component={NotFoundComponent} />
        </Switch>
      </BrowserRouter>
    </AppComponent>
  </Provider>
);

render(Main(), document.getElementById('app'));
