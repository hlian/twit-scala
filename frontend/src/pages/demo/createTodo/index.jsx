import React, { Component, PropTypes } from 'react';
import styled from 'styled-components';

import colors from 'app/styles/colors';
import { clearFix } from 'app/styles/mixins';

import TextInput from 'app/components/textInput';
import Button from 'app/components/button';

const CreateTodoForm = styled.form`
  ${clearFix()}
  width: 100%;
  position: relative;
  display: block;
`;

const TodoInput = styled(TextInput)`
  margin-bottom: 5px;
`;

const SubmitTodo = styled(Button)`
  float: right;
  border-radius: 0 0 5px 5px;

  &:disabled {
    background: ${colors.disabled};
  }
`;

class CreateTodo extends Component {
  constructor(props) {
    super(props);

    this.state = {
      input: '',
    };
  }

  createTodo = (e) => {
    e.preventDefault();
    if (this.state.input !== '') {
      this.props.create(this.state.input);
      this.setState({ input: '' });
    }
  }

  render() {
    const { input } = this.state;

    return (
      <CreateTodoForm onSubmit={this.createTodo}>
        <TodoInput onChange={e => this.setState({ input: e.target.value })} value={input} />
        <SubmitTodo disabled={input.length === 0}>Create</SubmitTodo>
      </CreateTodoForm>
    );
  }

}

CreateTodo.propTypes = {
  create: PropTypes.func,
};

export default CreateTodo;
