package com.chowscott.todo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class TodoTaskController {

  @Autowired
  private TodoTaskRepository repository;

  @GetMapping(value="/")
  public String landing(Model model) {
    ArrayList<TodoTask> todoList = (ArrayList<TodoTask>) repository.findAll();
    model.addAttribute("items", new TodoTaskResource(todoList));
    model.addAttribute("newItem", new TodoTask());
    return "landing";
  }

  @PostMapping(value="/add")
  public String addTodo(@ModelAttribute TodoTask todoTask) {
    TodoTask newTask = new TodoTask(todoTask.getName());
    repository.save(newTask);
    return "redirect:/";
  }

  @PostMapping(value="/update")
  public String updateTodo(@ModelAttribute TodoTaskResource todoTaskList) {
    for (TodoTask task : todoTaskList.getTodoList()) {
      TodoTask updateTask = new TodoTask(task.getName());
      updateTask.setComplete(task.isComplete());
      updateTask.setId(task.getId());
      repository.save(updateTask);
    }
    return "redirect:/";
  }

  @PostMapping(value="/delete/{id}")
  public String deleteTodo(@PathVariable("id") long id) {
    repository.deleteById(id);
    return "redirect:/";
  }

  @GetMapping(value="/greeting")
  public String getGreeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
      model.addAttribute("name", name);
      return "greeting";
  }
  
}