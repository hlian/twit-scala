{shallow} = require 'enzyme'
e = require 'react-e'
FilteredTodoList = require './'


clickOnLinkByText = (wrapper, text) ->
  wrapper
    .find 'Link'
    .filterWhere (n) -> n.props().children is text
    .simulate 'click'


getActiveLinkText = (wrapper) ->
  wrapper
    .find 'Link'
    .filterWhere (n) -> n.props().active
    .props().children


describe 'components - FilteredTodoList', ->
  beforeEach ->
    @wrapper = shallow e FilteredTodoList,
      todos: [
        {completed: true, text: 'write tests'}
        {completed: false, text: 'write code'}
      ]

  it 'makes Active the only active link', ->
    expect(getActiveLinkText(@wrapper)).to.eql 'Active'

  it 'passes the uncompleted todos to TodoList', ->
    expect(@wrapper.find('TodoList').props().todos).to.eql [
      {completed: false, text: 'write code'}
    ]

  describe 'clicking on All link', ->
    beforeEach ->
      clickOnLinkByText @wrapper, 'All'

    it 'makes All the only active link', ->
      expect(getActiveLinkText(@wrapper)).to.eql 'All'

    it 'pass all the todos to TodoList', ->
      expect(@wrapper.find('TodoList').props().todos).to.eql [
        {completed: true, text: 'write tests'}
        {completed: false, text: 'write code'}
      ]

  describe 'clicking on Completed link', ->
    beforeEach ->
      clickOnLinkByText @wrapper, 'Completed'

    it 'makes Completed the only active link', ->
      expect(getActiveLinkText(@wrapper)).to.eql 'Completed'

    it 'passes the completed todos to TodoList', ->
      expect(@wrapper.find('TodoList').props().todos).to.eql [
        {completed: true, text: 'write tests'}
      ]
