package com.nicusor.BlogApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicusor.BlogApp.model.Comment;
import com.nicusor.BlogApp.model.Post;
import com.nicusor.BlogApp.service.CommentService;
import com.nicusor.BlogApp.service.JwtService;
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


@WebMvcTest(controllers = CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommentService commentService;
    @MockBean
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllCommentsForPost() throws Exception{
        Post post = Post.builder()
                .id(1L)
                .title("Unit test your code")
                .body("Unit testing is very important")
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .body("Yeah, I <3 unit testing")
                .post(post)
                .build();

        when(commentService.getAllCommentsForPost(post.getId()))
                .thenReturn(new ArrayList<>(List.of(comment)));

        ResultActions response = mockMvc
                .perform(get("/api/v1/posts/" + post.getId() + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.size()", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$[0].body", CoreMatchers.is(comment.getBody())));
    }

    @Test
    public void testCreateCommentForPost() throws Exception{
        Post post = Post.builder()
                .id(1L)
                .title("Unit test your code")
                .body("Unit testing is very important")
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .body("Yeah, I <3 unit testing")
                .build();

        when(commentService.createCommentForPost(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(comment);

        ResultActions response = mockMvc
                .perform(post("/api/v1/posts/" + post.getId() + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(comment)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.body", CoreMatchers.is(comment.getBody())));
    }

    @Test
    public void testUpdateCommentForPost() throws Exception {
        Post post = Post.builder()
                .id(1L)
                .title("Unit test your code")
                .body("Unit testing is very important")
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .body("Yeah, I <3 unit testing")
                .post(post)
                .build();

        when(commentService.updateCommentForPost(
                ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(comment);

        ResultActions response = mockMvc
                .perform(put(
                        "/api/v1/posts/" + post.getId() +
                        "/comments/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(comment)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.body", CoreMatchers.is(comment.getBody())));
    }

    @Test
    public void testDeleteCommentForPost() throws Exception{
        doNothing().when(commentService)
                .deleteCommentForPost(ArgumentMatchers.any(), ArgumentMatchers.any());

        ResultActions response = mockMvc
                .perform(delete("/api/v1/posts/" + 1L + "/comments/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(
                        "Comment deleted successfully."));
    }
}