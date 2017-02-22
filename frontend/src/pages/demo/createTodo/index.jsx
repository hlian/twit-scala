import React, { Component, PropTypes } from 'react';
import styled from 'styled-components';

import { colors } from 'app/styles/variables';
import { sansFont } from 'app/styles/fonts';
import { clearFix } from 'app/styles/mixins';

const CreateTodoForm = styled.form`
  ${clearFix()}
  width: 100%;
  position: relative;
  display: block;
`;

const padding = `
  padding: 5px;
  margin-bottom: 5px;
  box-sizing: border-box;
`;

const TodoInput = styled.input`
  ${padding}
  ${sansFont(300)}
  font-size: 18px;
  display: block;
  width: 100%;
`;

const SubmitTodo = styled.button`
  ${padding}
  float: right;
  width: 100px;
  background: ${colors.primary};
  border-radius: 0 0 5px 5px;

  &:disabled {
    background: ${colors.greyD};
  }
`;


class CreateTodo extends Component {
  constructor(props) {
    super(props);

    this.state = {
      input: '',
    };
  }

  updateInput = (e) => {
    this.setState({ input: e.target.value });
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
        <TodoInput type="text" onChange={this.updateInput} value={input} />
        <SubmitTodo disabled={input.length === 0}>Create</SubmitTodo>
      </CreateTodoForm>
    );
  }

}

CreateTodo.propTypes = {
  create: PropTypes.func,
};

export default CreateTodo;
