import React from 'react';
import { NavLink } from 'react-router-dom';
import styled from 'styled-components';

const StyledLink = styled(NavLink)`
  text-decoration: underline;
  font-weight: 400;
`;


const NavigationLink = props =>
  React.createElement(StyledLink, { activeClassName: 'active', ...props });

export default NavigationLink;
