import React, { Component, PropTypes } from 'react';
import Snap from 'snapsvg';
import styled from 'styled-components';

import colors from 'app/styles/colors';

const SVG = styled.svg`
  display: inline-block;
`;

class ProgressBar extends Component {

  constructor(props) {
    super(props);

    this.state = {
      height: 20,
      width: 200,
      animationTime: 1000,
      progressBar: null,
    };
  }

  componentDidMount() {
    this.drawProgress();
  }

  componentDidUpdate(prevProps) {
    if (prevProps.progress !== this.props.progress) {
      this.updateProgressBar(this.state.progressBar, this.props.progress);
    }
  }

  drawProgress() {
    const { height, width } = this.state;

    const s = Snap(this.svg);

    const backgroundBar = s.rect(0, 0, width, height);

    backgroundBar.attr({
      stroke: 'none',
      fill: colors.background,
    });

    const progressBar = s.rect(0, 0, 0, height);

    this.setState({ progressBar });
    this.updateProgressBar(progressBar, this.props.progress);
  }

  updateProgressBar(progressBar, progress) {
    const { animationTime, width } = this.state;

    progressBar.animate({ width: width * progress }, animationTime * progress);
    this.setState({ progressBar });
  }

  render() {
    const { width, height } = this.state;
    return (
      <SVG innerRef={c => (this.svg = c)} width={width} height={height} />
    );
  }
}

ProgressBar.propTypes = {
  progress: PropTypes.number.isRequired,
};

export default ProgressBar;
