package com.chowscott.todo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TodoTask {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String name;
  private boolean complete;

  public TodoTask() {}

  public TodoTask(String name) {
    this.name = name;
    this.complete = false;
  }

  @Override
  public String toString() {
    return String.format("TodoTask[ id=%d | name='%s' | complete='%b' ", id, name, complete);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isComplete() {
    return complete;
  }

  public void setComplete(boolean complete) {
    this.complete = complete;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

}