package com.nicusor.BlogApp.controller;

import com.nicusor.BlogApp.model.Post;
import com.nicusor.BlogApp.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public Post getPostDetails(@PathVariable("id") Long postId){
        return postService.getPostDetails(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody @Valid Post postData){
        return postService.create(postData);
    }

    @PutMapping("/{id}")
    public Post updatePost(
            @PathVariable("id") Long postId,
            @RequestBody @Valid Post postData
    ){
        return postService.update(postId, postData);
    }

    @DeleteMapping("/{id}")
    public String deletePost(
            @PathVariable("id") Long postId
    ){
        postService.delete(postId);
        return "Post deleted successfully.";
    }
}
