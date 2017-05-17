import React, { Component, PropTypes } from 'react';
import Snap from 'snapsvg';
import styled from 'styled-components';

import colors from 'app/styles/colors';
import { absolutePosition } from 'app/styles/mixins';

const Wrapper = styled.div`
  position: relative;
  display: inline-block;
  width: ${props => (props.small ? props.size * 2 : props.size)}px;
  height: ${props => props.size}px;
`;

const SVG = styled.svg`${absolutePosition()}`;

const TextWrapper = styled.div`
  ${absolutePosition()}
  top: ${props => (props.size / 2) - 15}px;
  margin: ${props => (props.small ? '0' : 'auto')};
  width: ${props => (props.small ? '0' : '100%')};
  left: ${props => (props.small ? props.size * 1.2 : 0)}px;
  text-align: center;
`;

const TextLabel = styled.span`
  font-weight: 200;
  display: block;
  font-size: 12px;
  font-weight: 500;
  text-decoration: underline;
  color: ${colors.background};
  margin-top: -10px;
`;

const TextValue = styled.span`
  font-weight: 400;
  display: block;
  font-size: 24px;
`;

class DonutChart extends Component {

  constructor(props) {
    super(props);

    const size = props.small ? 75 : 150;
    const stroke = props.small ? 5 : 10;

    this.state = {
      size,
      stroke,
      center: size / 2,
      radius: (size - stroke) / 2,
      totalAnimationTime: 1500,
    };
  }

  componentDidMount() {
    this.drawChart();
  }

  drawChart() {
    const { center, radius, stroke, totalAnimationTime } = this.state;
    const { start, end, color } = this.props;

    const s = Snap(this.svg);

    const circle = s.circle(center, center, radius);
    circle.attr({
      stroke: '#BDBDBD',
      fill: 'none',
      strokeWidth: 1,
    });

    const animationLength = totalAnimationTime * (end - start);
    const startTime = totalAnimationTime * start * 0.8;

    const animateArc = () => {
      let arc = s.path('');
      const arcAttributes = { stroke: color, fill: 'none', strokeWidth: stroke };

      Snap.animate(start, end, (val) => {
        arc.remove();
        arc = s.path(this.calcArc(start, val));
        arc.attr(arcAttributes);
      }, animationLength);
    };

    setTimeout(animateArc, startTime);
  }


  /**
   * https://developer.mozilla.org/en-US/docs/Web/SVG/Tutorial/Paths
   * Using SVG paths for the arc the commands used are:
   *   move to: M start-x start-y
   *   elliptical arc: A radius-x radiux-y x-rotation large-arc-flag sweep flag end-x end-y
   */
  calcArc(startPercent, endPercent) {
    const { center, radius } = this.state;

    const startPoint = startPercent * 360;
    const endpoint = endPercent * 360;

    const startRadians = (Math.PI * (startPoint - 90)) / 180;
    const startX = center + (radius * Math.cos(startRadians));
    const startY = center + (radius * Math.sin(startRadians));

    const endRadians = (Math.PI * (endpoint - 90)) / 180;
    const endX = center + (radius * Math.cos(endRadians));
    const endY = center + (radius * Math.sin(endRadians));

    const largeArcFlag = (endpoint - startPoint) > 180 ? 1 : 0;

    const pathStart = `M ${startX} ${startY}`;
    const pathArc = `A ${radius} ${radius} 0 ${largeArcFlag} 1 ${endX} ${endY}`;

    return `${pathStart} ${pathArc}`;
  }

  renderText = () => {
    const { size } = this.state;
    const { small, value, label } = this.props;
    if (!small) {
      const labelSpan = label ? <TextLabel>{label}</TextLabel> : null;
      return (
        <TextWrapper size={size}>
          {labelSpan}
          <TextValue>{value}</TextValue>
        </TextWrapper>
      );
    }
    return (
      <TextWrapper small size={size}>
        <TextValue>{value}</TextValue>
      </TextWrapper>
    );
  }

  render() {
    const { size } = this.state;
    const { small } = this.props;

    return (
      <Wrapper size={size} small={small}>
        <SVG innerRef={c => (this.svg = c)} width={size} height={size} />
        {this.renderText()}
      </Wrapper>
    );
  }
}

DonutChart.propTypes = {
  start: PropTypes.number.isRequired,
  end: PropTypes.number.isRequired,
  value: PropTypes.string,
  label: PropTypes.string,
  color: PropTypes.string,
  small: PropTypes.bool,
};

DonutChart.defaultProps = {
  color: colors.primary,
  small: false,
};

export default DonutChart;
