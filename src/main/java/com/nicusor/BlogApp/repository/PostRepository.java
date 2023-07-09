package com.nicusor.BlogApp.repository;

import com.nicusor.BlogApp.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    public Optional<Post> findByTitle(String title);
    public List<Post> findByTitleContainingIgnoreCase(String title);
}
