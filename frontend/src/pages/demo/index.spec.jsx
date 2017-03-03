import React from 'react';
import { mount } from 'enzyme';

import List, { components } from 'app/components/list';
import { TodoComponent } from './';

const findTodos = wrapper => wrapper.find(components.CheckboxEntry);

const inputTodoText = (wrapper, text) =>
  wrapper
    .find('CreateTodo input')
    .simulate('change', { target: { value: text } });

const submitTodo = wrapper =>
  wrapper
    .find('CreateTodo button')
    .simulate('submit');

describe('TodoComponent', () => {

  beforeEach(function() {
    this.todos = [
      { id: 1, todo: 'write tests', completed: true },
      { id: 2, todo: 'write code', completed: false },
      { id: 3, todo: 'pass tests', completed: false },
    ];
    this.toggleTodo = sinon.stub();
    this.createTodo = sinon.stub();
    this.wrapper = mount(
      <TodoComponent
        todos={this.todos}
        toggleTodo={this.toggleTodo}
        createTodo={this.createTodo}
      />
    );
  });

  it('should show all todos by default', function() {
    expect(findTodos(this.wrapper)).to.have.length(3);
  });

  describe('filters', () => {
    beforeEach(function() {
      this.wrapper.setState({ filter: 'completed' });
    });

    it('should only display the appropriate todos', function() {
      expect(this.wrapper.find(List).props().entries).to.eql([
        { id: 1, value: 'write tests', checked: true },
      ]);
    });
  });

  describe('checking a todo', () => {
    beforeEach(function() {
      this.wrapper
        .find('List input[type="checkbox"]')
        .at(1)
        .simulate('change');
    });

    it('should make a call to toggle the todo', function() {
      expect(this.toggleTodo).to.have.been.calledOnce;
    });
  });

  describe('creating a todo', () => {

    describe('creating an empty todo', () => {
      beforeEach(function() {
        submitTodo(this.wrapper);
      });

      it('should not make a call to create the todo', function() {
        expect(this.createTodo).to.have.not.been.called;
      });

    });

    describe('creating a non-empty todo', () => {
      beforeEach(function() {
        inputTodoText(this.wrapper, 'New Todo');
        submitTodo(this.wrapper);
      });

      it('should make a call to create the todo', function() {
        expect(this.createTodo).to.have.been.calledWith('New Todo');
      });
    });

  });

});
