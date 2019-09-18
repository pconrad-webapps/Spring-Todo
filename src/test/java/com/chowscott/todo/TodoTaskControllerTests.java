package com.chowscott.todo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.mockito.BDDMockito.*;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class TodoTaskControllerTests {

  @MockBean
  private TodoTaskRepository repository;
  @Autowired
  private MockMvc mvc;

  @Test
  @WithMockUser
  public void landingShouldSucceedAndExist() throws Exception {
    mvc.perform(get("/").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
        .andExpect(content().string(containsString("This site is currently under construction.")));
  }

  @Test
  @WithMockUser
  public void landingForm() throws Exception {
    ArrayList<TodoTask> todoList = new ArrayList<TodoTask>();
    todoList.add(new TodoTask("task 1"));
    todoList.add(new TodoTask("task 2"));

    given(this.repository.findAll()).willReturn(todoList);
    mvc.perform(get("/").accept(MediaType.TEXT_HTML)).andExpect(content().string(containsString("todos")))
        .andExpect(content().string(containsString("new task goes here")))
        .andExpect(content().string(containsString("make"))).andExpect(content().string(containsString("task 1")))
        .andExpect(content().string(containsString("task 2")));
  }

  @Test
  @WithMockUser
  public void landingFormCreateTask() throws Exception {
    mvc.perform(get("/").accept(MediaType.TEXT_HTML)).andExpect(content().string(not(containsString("task 1"))));
    mvc.perform(post("/add").with(csrf().asHeader()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("name", "task 1").accept(MediaType.TEXT_HTML)).andExpect(status().is3xxRedirection());
    verify(this.repository, times(1)).save(new TodoTask("task 1"));
  }

  @Test
  @WithMockUser
  public void landingFormUpdateSpecificTask() throws Exception {
    given(this.repository.findById(1L)).willReturn(Optional.of(new TodoTask(1L, "task 1", false)));
    mvc.perform(post("/update").with(csrf().asHeader()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("updateId", "1")).andExpect(status().is3xxRedirection());

    verify(this.repository, times(1)).findById(1L);
    verify(this.repository, times(1)).save(new TodoTask(1L, "task 1", true));
  }

  @Test
  @WithMockUser
  public void landingFormDeleteSpecificTask() throws Exception {
    mvc.perform(post("/update").with(csrf().asHeader()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("removeId", "1")).andExpect(status().is3xxRedirection());
    verify(this.repository, times(1)).deleteById(1L);
  }

  @Test
  @WithMockUser
  public void updateTodoRemoveIdPrecendence() throws Exception {
    mvc.perform(post("/update").with(csrf().asHeader()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("removeId", "1").param("updateId", "1")).andExpect(status().is3xxRedirection());
    verify(this.repository, times(1)).deleteById(1L);
    verify(this.repository, never()).save(any());
  }
}
