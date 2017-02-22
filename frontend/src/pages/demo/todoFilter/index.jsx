import React, { PropTypes } from 'react';
import styled from 'styled-components';

import { colors } from 'app/styles/variables';
import { sansFont } from 'app/styles/fonts';

const Filter = styled.button`
  ${sansFont(400)}
  width: 100px;
  padding: 5px;
  margin-right: 5px;
  border-radius: 5px 5px 0 0;
  background: ${props => (props.active ? colors.primary : colors.greyD)};

  &:focus {
    outline: 0;
  }
`;

const TodoFilter = props =>
  <div>
    {props.filters.map(({ name, active, setFilter }) =>
      <Filter key={name} active={active} onClick={setFilter}>
        {name}
      </Filter>
    )}
  </div>;

TodoFilter.propTypes = {
  filters: PropTypes.arrayOf(PropTypes.shape({
    name: PropTypes.string,
    active: PropTypes.bool,
    setFilter: PropTypes.func,
  })),
};

export default TodoFilter;
