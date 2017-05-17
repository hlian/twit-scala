import { PropTypes } from 'react';
import styled from 'styled-components';

import colors from 'app/styles/colors';

const buttonStyles = ({ inverted }) => {
  let fg = colors.white;
  let bg = colors.primary;
  if (inverted) {
    [fg, bg] = [bg, fg];
  }

  return `
    background: ${bg};
    border: 1px solid ${fg};
    color: ${fg};
  `;
};

const Button = styled.button`
  font-weight: 200;
  cursor: pointer;
  fontSize: 1rem;
  outline: none;
  border-radius: 3px;
  padding: .5rem 2rem;
  box-sizing: border-box;
  ${buttonStyles}
`;

Button.propTypes = {
  inverted: PropTypes.bool,
};

export default Button;
