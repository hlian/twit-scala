import { keyframes } from 'styled-components';

const fadeIn = keyframes`
  from { opacity: 0; }
  to { opacity: 1; }
`;

const fadeOut = keyframes`
  from { opacity: 1; }
  to { opacity: 0; }
`;

const rotate360 = keyframes`
  from {
    transform: rotate(0deg);
  }

  to {
    transform: rotate(360deg);
  }
`;

export const animations = {
  fadeIn: `animation: ${fadeIn} .5s ease;`,
  fadeOut: `animation: ${fadeOut} .5s ease;`,
  quickFadeIn: `animation: ${fadeIn} .25s ease;`,
  quickFadeOut: `animation: ${fadeOut} .25s ease;`,
  rotate: `display: inline-block; animation: ${rotate360} 2s linear infinite;`,
};

export default animations;
