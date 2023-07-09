package com.nicusor.BlogApp.service;

import com.nicusor.BlogApp.exception.*;
import com.nicusor.BlogApp.model.Comment;
import com.nicusor.BlogApp.model.Post;
import com.nicusor.BlogApp.model.User;
import com.nicusor.BlogApp.repository.CommentRepository;
import com.nicusor.BlogApp.repository.PostRepository;
import com.nicusor.BlogApp.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    public List<Comment> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post with provided id does not exist"));

        return post.getComments();
    }

    public Comment createCommentForPost(Long postId, Comment commentData) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post with provided id does not exist"));

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with email " + userEmail + " does not exist"));

        commentData.setPost(post);
        commentData.setUser(user);
        return commentRepository.save(commentData);
    }


    public Comment updateCommentForPost(Long postId, Long commentId, Comment commentData) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post with provided id does not exist"));

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(
                        "Comment with provided id does not exist"));

        if(!comment.getPost().equals(post)){
            throw new CommentDoesNotBelongToPostException(
                    "This comment does not belong to this post");
        }

        if(!comment.getUser().getEmail().equals(userEmail)){
            throw new CommentDoesNotBelongToUser("You are not the owner of the comment");
        }

        comment.setBody(commentData.getBody());
        return commentRepository.save(comment);
    }

    public void deleteCommentForPost(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post with provided id does not exist"));

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(
                        "Comment with provided id does not exist"));

        if(!comment.getPost().equals(post)){
            throw new CommentDoesNotBelongToPostException(
                    "This comment does not belong to this post");
        }

        if(!comment.getUser().getEmail().equals(userEmail)){
            throw new CommentDoesNotBelongToUser("You are not the owner of the comment");
        }

        commentRepository.delete(comment);
    }
}
