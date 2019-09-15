package com.chowscott.todo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@RunWith(SpringRunner.class)
@WebMvcTest
public class GreetingControllerTests {

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
}