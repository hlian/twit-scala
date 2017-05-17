import React, { PropTypes } from 'react';
import styled from 'styled-components';

import colors from 'app/styles/colors';

const Tab = styled.div`
  font-weight: 400;
  display: inline-block;
  width: 100px;
  padding: .2rem .2rem .1rem;
  border-radius: 3px 3px 0 0;
  text-align: center;
  background: ${props => (props.active ? colors.primary : colors.greyD)};

  &:not(:last-child) {
    margin-right: 5px;
  }
`;

const Tabs = (props) => {
  const onClick = props.tabClick;
  return (
    <div>
      {props.tabs.map(({ id, value, active }) =>
        <Tab key={id} active={active} onClick={() => onClick(id)}>
          {value}
        </Tab>
      )}
    </div>
  );
};

Tabs.propTypes = {
  tabs: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.string,
    value: PropTypes.oneOfType([
      PropTypes.string,
      PropTypes.element,
    ]),
    active: PropTypes.bool,
  })),
  tabClick: PropTypes.func,
};

export default Tabs;
