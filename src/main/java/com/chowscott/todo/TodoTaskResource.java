package com.chowscott.todo;

import java.util.ArrayList;

import javax.validation.Valid;

public class TodoTaskResource {
  @Valid
  private ArrayList<TodoTask> todoList = new ArrayList<>();

  public TodoTaskResource() {}

  public TodoTaskResource(ArrayList<TodoTask> todoList) {
    this.todoList = todoList;
  }

  public ArrayList<TodoTask> getTodoList() {
    return todoList;
  }

  public void setTodoList(ArrayList<TodoTask> todoList) {
    this.todoList = todoList;
  }
}