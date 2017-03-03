import React from 'react';
import styled from 'styled-components';

import { sansFont } from 'app/styles/fonts';

const StyledInput = styled.input`
  ${sansFont(300)}
  font-size: 18px;
  display: block;
  width: 100%;
  padding: 5px 10px;
  box-sizing: border-box;
`;

const TextInput = props => <StyledInput type="text" {...props} />;

export default TextInput;
