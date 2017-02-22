import React, { PropTypes } from 'react';
import styled from 'styled-components';

const AppContainer = styled.div`
  width: 1080px;
  margin: auto;
`;

const AppComponent = props =>
  <AppContainer>
    {props.children}
  </AppContainer>;

AppComponent.propTypes = {
  children: PropTypes.element,
};

export default AppComponent;
