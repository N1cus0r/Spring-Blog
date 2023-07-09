package com.nicusor.BlogApp.controller;

import com.nicusor.BlogApp.model.Comment;
import com.nicusor.BlogApp.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("posts/{postId}/comments")
    public List<Comment> getAllCommentsForPost(
            @PathVariable("postId") Long postId
    ){
        return commentService.getAllCommentsForPost(postId);
    }

    @PostMapping("posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createCommentForPost(
            @PathVariable("postId") Long postId,
            @RequestBody @Valid Comment commentData
    ){
        return commentService.createCommentForPost(postId, commentData);
    }

    @PutMapping("posts/{postId}/comments/{commentId}")
    public Comment updateCommentForPost(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @RequestBody @Valid Comment commentData
    ){
        return commentService.updateCommentForPost(postId, commentId, commentData);
    }

    @DeleteMapping("posts/{postId}/comments/{commentId}")
    public String deleteCommentForPost(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId
    ){
        commentService.deleteCommentForPost(postId, commentId);
        return "Comment deleted successfully.";
    }
}
