import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import styled from 'styled-components';
import _ from 'lodash';

import { actions } from 'app/store/partitions';
import Tabs from 'app/components/tabs';
import List from 'app/components/list';

import CreateTodo from './createTodo';

const DemoContainer = styled.div`
  width: 350px;
  margin: auto;
`;

const ListContainer = styled(List)`
  width: 100%;
  height: 200px;
  overflow-y: scroll;
  margin-bottom: 10px;
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
        id: filter,
        active: this.state.filter === filter,
        value: filter,
      };
    });
  }

  filterTodos() {
    const { todos } = this.props;
    const mapTodos = (todo) => {
      return {
        id: todo.id,
        checked: todo.completed,
        value: todo.todo,
      };
    };

    switch (this.state.filter) {
      case 'completed': return _.filter(todos, ['completed', true]).map(mapTodos);
      case 'active': return _.filter(todos, ['completed', false]).map(mapTodos);
      default: return _.map(todos, mapTodos);
    }
  }

  render() {
    const { toggleTodo, createTodo } = this.props;
    return (
      <DemoContainer>
        <Tabs tabs={this.filters()} tabClick={filter => this.setState({ filter })} />
        <ListContainer entries={this.filterTodos()} toggle={todo => toggleTodo(todo.id)} />
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
    todos: state.todos.todos,
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
