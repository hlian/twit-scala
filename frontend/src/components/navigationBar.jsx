import React, { PropTypes } from 'react';
import styled from 'styled-components';

import colors from 'app/styles/colors';
import widths from 'app/styles/widths';

const NavbarWrapper = styled.div`
  width: 100%;
  background: ${colors.primary};
  color: ${colors.black};
  padding: 10px;
`;

const Contents = styled.div`
  width: ${widths.full}px;
  margin: auto;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
`;

const Navbar = props =>
  <NavbarWrapper className={props.className}>
    <Contents>
      {props.children}
    </Contents>
  </NavbarWrapper>;

Navbar.propTypes = {
  className: PropTypes.string,
  children: PropTypes.arrayOf(PropTypes.element),
};

export default Navbar;
