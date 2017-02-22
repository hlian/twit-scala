import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import styled from 'styled-components';
import _ from 'lodash';

import actions from 'app/store/actions';
import TodoList from './todoList';
import CreateTodo from './createTodo';
import TodoFilter from './todoFilter';

const DemoContainer = styled.div`
  width: 350px;
  margin: auto;
`;

class TodoComponent extends Component {

  constructor(props) {
    super(props);

    this.state = {
      filter: 'all',
    };
  }

  filters() {
    const filters = ['all', 'completed', 'active'];
    return filters.map((filter) => {
      return {
        name: filter,
        active: this.state.filter === filter,
        setFilter: () => this.setState({ filter }),
      };
    });
  }

  filterTodos() {
    const { todos } = this.props;
    switch (this.state.filter) {
      case 'completed': return _.filter(todos, ['completed', true]);
      case 'active': return _.filter(todos, ['completed', false]);
      default: return todos;
    }
  }

  render() {
    const { toggleTodo, createTodo } = this.props;
    return (
      <DemoContainer>
        <TodoFilter filters={this.filters()} />
        <TodoList todos={this.filterTodos()} toggleTodo={toggleTodo} />
        <CreateTodo create={createTodo} />
      </DemoContainer>
    );
  }
}

TodoComponent.propTypes = {
  todos: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.number,
    todo: PropTypes.string,
    completed: PropTypes.bool,
  })),
  toggleTodo: PropTypes.func,
  createTodo: PropTypes.func,
};

const mapState = (state) => {
  return {
    todos: state.todos,
  };
};

const mapDispatch = (dispatch) => {
  return {
    toggleTodo: todo => dispatch(actions.todos.toggleComplete(todo)),
    createTodo: text => dispatch(actions.todos.create(text)),
  };
};


const TodoContainer = connect(mapState, mapDispatch)(TodoComponent);

export { TodoComponent, TodoContainer };
