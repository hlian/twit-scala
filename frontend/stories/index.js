import React from 'react';
import { storiesOf, action } from '@kadira/storybook';

import DonutChart from 'app/components/donutChart';
import ProgressBar from 'app/components/progressBar';
import Button from 'app/components/button';
import TextInput from 'app/components/textInput';
import List from 'app/components/list';

storiesOf('Donut Chart', module)
  .add('Simple Chart', () => (
    <div>
      <DonutChart start={0.2} end={0.65} />
      <DonutChart start={0.2} end={0.65} value={"45"} />
      <DonutChart start={0.2} end={0.65} value={"45"} label={"apples"} />
    </div>
  ))
  .add('Small Chart', () => (
    <div>
      <DonutChart start={0.0} end={0.2} small />
      <DonutChart start={0.2} end={0.65} small value={"45"} />
      <DonutChart start={0.65} end={1.0} small value={"45"} />
    </div>
  ))
  .add('Timed Charts', () => (
    <div>
      <DonutChart start={0.0} end={0.20} />
      <DonutChart start={0.2} end={0.5} />
      <DonutChart start={0.5} end={0.7} />
      <DonutChart start={0.7} end={1.0} />
    </div>
  ))

class AnimatedBar extends React.Component {
  constructor(props) {
   super(props)
    this.state = {
      progress: 0.2,
    };

    setTimeout((() => this.setState({ progress: 1})), 2000);
  }

  render() {
    return <ProgressBar progress={this.state.progress} />
  }
}

storiesOf('Progress Bar', module)
  .add('Progress Bar', () => (
    <ProgressBar progress={.5} />
  ))
  .add('Animated Progress Bar', () => (
    <AnimatedBar />
  ))

storiesOf('Button', module)
  .add('Normal', () => (
    <div>
      <Button>Normal Button</Button>
      <Button inverted>Inverted Button</Button>
    </div>
  ))

storiesOf('Text Input', module)
  .add('Normal', () => (
    <div>
      <TextInput />
      <TextInput defaultValue="with default value" />
      <TextInput placeholder="with default placeholder" />
    </div>
  ))

storiesOf('List', module)
  .add('CheckList', () => {
    const entries = [
      {id: 1 ,value: 'foo', checked: false},
      {id: 2 ,value: 'bar', checked: false},
      {id: 3 ,value: 'bax', checked: true},
    ]

    return(
      <List entries={entries} toggle={action('toggle')}/>
    )
  })
