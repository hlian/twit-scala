import React, { PropTypes } from 'react';
import styled from 'styled-components';

import colors from 'app/styles/colors';

const ListWrapper = styled.div`
  box-sizing: border-box;
  border: 1px ${colors.greyD} solid;
`;

const BaseEntry = styled.div`
  font-weight: 300;
  font-size: 18px;
  padding: 10px 5px;

  &:nth-of-type(2n) {
    background: ${colors.greyD}
  }
`;

const ListEntry = styled(BaseEntry)`
  input {
    margin-right: 10px;
  }
`;

const CheckboxEntry = props =>
  <ListEntry>
    <input type="checkbox" checked={props.checked} onChange={() => props.toggle(props)} />
    {props.value}
  </ListEntry>;

const List = props =>
  <ListWrapper {...props}>
    {props.entries.map(entry =>
      React.createElement(CheckboxEntry, { key: entry.id, toggle: props.toggle, ...entry })
    )}
  </ListWrapper>;

const entryProp = {
  id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  checked: PropTypes.bool,
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
};

CheckboxEntry.propTypes = {
  toggle: PropTypes.func,
  ...entryProp,
};

List.propTypes = {
  entries: PropTypes.arrayOf(PropTypes.shape(entryProp)),
  toggle: PropTypes.func,
};

export const components = { CheckboxEntry };
export default List;
