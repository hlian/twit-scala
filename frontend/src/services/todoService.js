class TodoService {
  constructor() {
    this.nextId = 1;
  }

  create(text) {
    const id = this.nextId;
    this.nextId = this.nextId + 1;
    return { id, todo: text, completed: false };
  }
}

export default TodoService;
