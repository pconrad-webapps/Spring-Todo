package com.chowscott.todo;

import java.security.Principal;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TodoTaskController {

  @Autowired
  private TodoTaskRepository repository;

  @GetMapping(value = "/")
  public String landing(Model model) {
    ArrayList<TodoTask> todoList = (ArrayList<TodoTask>) repository.findAll();
    model.addAttribute("items", new TodoTaskResource(todoList));
    model.addAttribute("newItem", new TodoTask());
    return "landing";
  }

  @GetMapping(value = "/users")
  public String users(Principal principal) {
    return "landing";
  }

  @PostMapping(value = "/add")
  public String addTodo(@ModelAttribute TodoTask todoTask) {
    TodoTask newTask = new TodoTask(todoTask.getName());
    repository.save(newTask);
    return "redirect:/";
  }

  @PostMapping(value = "/update")
  public String updateTodo(@ModelAttribute TodoTaskResource todoTaskList) {
    for (TodoTask task : todoTaskList.getTodoList()) {
      TodoTask updateTask = new TodoTask(task.getName());
      updateTask.setComplete(task.isComplete());
      updateTask.setId(task.getId());
      repository.save(updateTask);
    }
    return "redirect:/";
  }

  @PostMapping(value= "/update", params = {"updateId"})
  public String updateTodo(final TodoTaskResource todoList, final BindingResult bindingResult, final HttpServletRequest req) {
    final Long id = Long.valueOf(req.getParameter("updateId"));
    TodoTask task = repository.findById(id).get();
    if (task != null) {
      task.setComplete(!task.isComplete());
      repository.save(task);
    }

    return "redirect:/";
  }

  @PostMapping(value= "/update", params = {"removeId"})
  public String updateRemoveTodo(final TodoTaskResource todoList, final BindingResult bindingResult, final HttpServletRequest req) {
    final Long id = Long.valueOf(req.getParameter("removeId"));
    repository.deleteById(id);
    return "redirect:/";
  }

  @PostMapping(value = "/delete/{id}")
  public String deleteTodo(@PathVariable("id") long id) {
    repository.deleteById(id);
    return "redirect:/";
  }

  @GetMapping(value = "/greeting")
  public String getGreeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
      Model model) {
    model.addAttribute("name", name);
    return "greeting";
  }

}
