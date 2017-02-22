import React from 'react';
import { Link } from 'react-router';
import styled from 'styled-components';

const StyledLink = styled(Link)`
  text-decoration: underline;
  font-weight: 400;
`;


const NavigationLink = props =>
  React.createElement(StyledLink, { activeClassName: 'active', ...props });

export default NavigationLink;
