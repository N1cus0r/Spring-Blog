package com.nicusor.BlogApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts", uniqueConstraints = @UniqueConstraint(columnNames = "title"))
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty(message = "Title is mandatory")
    @Size(max = 150)
    private String title;
    @NotEmpty(message = "Body is mandatory")
    private String body;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date dateCreated;
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date dateUpdated;
    @ManyToOne
    private User user;
    @JsonIgnore
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
}
