import React from 'react';
import styled from 'styled-components';

import colors from 'app/styles/colors';
import transitions from 'app/styles/transitions';

const StyledInput = styled.input`
  ${transitions.focus('color')}
  ${transitions.focus('border-color')}
  border-radius: 3px;
  border: 1px solid ${colors.border};
  box-sizing: border-box;
  color: ${colors.text};
  font-weight: 500;
  outline: none;
  padding: 10px;

  &::placeholder {
    color: ${colors.lightText};
  }

  &:focus {
    border-color: ${colors.text};
  }
`;

const TextInput = props => <StyledInput type="text" {...props} />;

export default TextInput;
