package com.nicusor.BlogApp.service;

import com.nicusor.BlogApp.exception.PostNotFoundException;
import com.nicusor.BlogApp.exception.PostDoesNotBelongToUser;
import com.nicusor.BlogApp.exception.PostTitleExists;
import com.nicusor.BlogApp.model.Post;
import com.nicusor.BlogApp.model.User;
import com.nicusor.BlogApp.repository.PostRepository;
import com.nicusor.BlogApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    public Post getPostDetails(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post with provided id does not exist"));
    }
    public Post create(Post postData) {
        if(!postRepository.findByTitle(postData.getTitle()).isEmpty()){
            throw new PostTitleExists("Post with provided title already exists");
        }

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with email " + userEmail + " does not exist"));

        postData.setUser(user);
        return postRepository.save(postData);
    }
    public Post update(Long postId, Post postData){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post with provided id does not exist"));

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!userEmail.equals(post.getUser().getEmail())){
            throw new PostDoesNotBelongToUser("You are not the owner of the post");
        }

        if(!postRepository.findByTitle(postData.getTitle()).isEmpty()){
            throw new PostTitleExists("Post with provided title already exists");
        }

        post.setTitle(postData.getTitle());
        post.setBody(postData.getBody());
        return postRepository.save(post);
    }
    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(
                        "Post with provided id does not exist"));

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!userEmail.equals(post.getUser().getEmail())){
            throw new PostDoesNotBelongToUser("You are not the owner of the post");

        }

        postRepository.delete(post);
    }
}
