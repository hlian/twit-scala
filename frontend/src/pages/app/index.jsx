import React, { PropTypes } from 'react';
import styled from 'styled-components';

import widths from 'app/styles/widths';
import Header from 'app/components/header';
import Footer from 'app/components/footer';

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
`;

const Contents = styled.div`
  width: ${widths.full}px;
  margin: 10px auto;
  flex: 1 1 auto;
`;

const AppComponent = props =>
  <Wrapper>
    <Header />
    <Contents> {props.children} </Contents>
    <Footer />
  </Wrapper>;

AppComponent.propTypes = {
  children: PropTypes.element,
};

export default AppComponent;
