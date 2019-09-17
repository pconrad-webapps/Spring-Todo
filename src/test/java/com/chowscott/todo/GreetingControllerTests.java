package com.chowscott.todo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.*;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest
public class GreetingControllerTests {

  @MockBean
  private TodoTaskRepository repository;
  @Autowired
  private MockMvc mvc;

  @Test
  public void greetingShouldSucceed() throws Exception {
    mvc.perform(get("/greeting").accept(MediaType.TEXT_HTML)).andExpect(status().isOk());
  }

  @Test
  public void greetingShouldCustomize() throws Exception {
    mvc.perform(get("/greeting").param("name", "Scott").accept(MediaType.TEXT_HTML)).andExpect(content().string(containsString("Hello, Scott!")));
  }

  @Test
  public void landingShouldSucceedAndExist() throws Exception {
    mvc.perform(get("/").accept(MediaType.TEXT_HTML))
      .andExpect(status().isOk())
      .andExpect(content().string(containsString("This site is currently under construction.")));
  }

  @Test
  public void landingForm() throws Exception {
    ArrayList<TodoTask> todoList = new ArrayList<TodoTask>();
    todoList.add(new TodoTask("task 1"));
    todoList.add(new TodoTask("task 2"));

    given(this.repository.findAll()).willReturn(todoList);
    mvc.perform(get("/").accept(
      MediaType.TEXT_HTML
    ))
      .andExpect(content().string(containsString("todos")))
      .andExpect(content().string(containsString("new task goes here")))
      .andExpect(content().string(containsString("make")))
      .andExpect(content().string(containsString("task 1")))
      .andExpect(content().string(containsString("task 2")))
      .andExpect(content().string(containsString("update tasks")));
  }

  @Test
  public void landingFormCreateTask() throws Exception {
    mvc.perform(get("/").accept(
      MediaType.TEXT_HTML
    ))
      .andExpect(content().string(not(containsString("task 1"))));
    mvc.perform(post("/add")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("name", "task 1")
      .accept(
        MediaType.TEXT_HTML
      ))
      .andExpect(status().is3xxRedirection());
    verify(this.repository, times(1)).save(new TodoTask("task 1"));    
  }

  @Test

  public void landingFormUpdateTasks() throws Exception {
    mvc.perform(post("/update")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("todoList[0].name", "task 1")
      .param("todoList[0].complete", "false")
      .param("todoList[0].id", "1")
      .param("todoList[1].name", "task 2")
      .param("todoList[1].complete", "true")
      .param("todoList[1].id", "2")
    )
      .andExpect(status().is3xxRedirection());
    verify(this.repository, times(1)).save(new TodoTask(1L, "task 1", false));
    verify(this.repository, times(1)).save(new TodoTask(2L, "task 2", true));
  }

  @Test
  public void landingFormDeleteTask() throws Exception {
    mvc.perform(post("/delete/1"))
      .andExpect(status().is3xxRedirection());
    verify(this.repository, times(1)).deleteById(1L);
  }
}
