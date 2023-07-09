package com.nicusor.BlogApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicusor.BlogApp.model.Post;
import com.nicusor.BlogApp.service.JwtService;
import com.nicusor.BlogApp.service.PostService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = PostController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PostService postService;
    @MockBean
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllPosts() throws Exception{
        Post post = Post.builder()
                .id(1L)
                .title("Unit test your code")
                .body("Unit testing is very important")
                .build();

        when(postService.getAllPosts())
                .thenReturn(new ArrayList<>(List.of(post)));

        ResultActions response = mockMvc
                .perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.size()", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[0].title", CoreMatchers.is(post.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[0].body", CoreMatchers.is(post.getBody())));
    }

    @Test
    public void testGetPostDetails() throws Exception{
        Post post = Post.builder()
                .id(1L)
                .title("Unit test your code")
                .body("Unit testing is very important")
                .build();

        when(postService.getPostDetails(post.getId()))
                .thenReturn(post);

        ResultActions response = mockMvc
                .perform(get("/api/v1/posts/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.title", CoreMatchers.is(post.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.body", CoreMatchers.is(post.getBody())));
    }

    @Test
    public void testCreatePost() throws Exception{
        Post post = Post.builder()
                .id(1L)
                .title("Unit test your code")
                .body("Unit testing is very important")
                .build();

        when(postService.create(ArgumentMatchers.any()))
                .thenReturn(post);

        ResultActions response = mockMvc
                .perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(post)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.title", CoreMatchers.is(post.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.body", CoreMatchers.is(post.getBody())));
    }

    @Test
    public void testUpdatePost() throws Exception{
        Post post = Post.builder()
                .id(1L)
                .title("Unit test your code")
                .body("Unit testing is very important")
                .build();

        when(postService.update(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(post);

        ResultActions response = mockMvc
                .perform(put("/api/v1/posts/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(post)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.title", CoreMatchers.is(post.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.body", CoreMatchers.is(post.getBody())));
    }

    @Test
    public void testDeletePost() throws Exception{
        doNothing().when(postService).delete(ArgumentMatchers.any());

        ResultActions response = mockMvc
                .perform(delete("/api/v1/posts/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        "Post deleted successfully."));
    }
}