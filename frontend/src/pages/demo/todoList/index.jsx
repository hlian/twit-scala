import React, { PropTypes } from 'react';
import styled from 'styled-components';

import { colors } from 'app/styles/variables';
import { sansFont } from 'app/styles/fonts';

const TodoContainer = styled.div`
  width: 100%;
  box-sizing: border-box;
  height: 200px;
  overflow-y: scroll;
  border: 1px ${colors.greyD} solid;
  margin-bottom: 10px;
`;

const TodoEntry = styled.div`
  ${sansFont(300)}
  font-size: 18px;
  padding: 10px 5px;

  &:nth-of-type(2n) {
    background: ${colors.greyD};
  }

  input {
    margin-right: 10px;
  }
`;


const ListEntry = props =>
  <TodoEntry>
    <input
      type="checkbox"
      checked={props.completed}
      onChange={() => props.toggleTodo(props)}
    />
    {props.todo}
  </TodoEntry>;

const TodoList = props =>
  <TodoContainer>
    {props.todos.map(todo =>
      React.createElement(ListEntry, { toggleTodo: props.toggleTodo, key: todo.id, ...todo })
    )}
  </TodoContainer>;

ListEntry.propTypes = {
  toggleTodo: PropTypes.func,
  id: PropTypes.number,
  todo: PropTypes.string,
  completed: PropTypes.bool,
};

TodoList.propTypes = {
  toggleTodo: PropTypes.func,
  todos: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.number,
    todo: PropTypes.string,
    completed: PropTypes.bool,
  })),
};

export const components = { TodoEntry };

export default TodoList;
