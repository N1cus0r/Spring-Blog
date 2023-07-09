package com.nicusor.BlogApp.service;

import com.nicusor.BlogApp.model.Comment;
import com.nicusor.BlogApp.model.Post;
import com.nicusor.BlogApp.model.User;
import com.nicusor.BlogApp.repository.CommentRepository;
import com.nicusor.BlogApp.repository.PostRepository;
import com.nicusor.BlogApp.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    private MockedStatic<SecurityContextHolder> mockedStatic;
    @Mock
    private UserRepository userRepository;
    private User user;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentService commentService;

    private boolean testRequiresAuthentication(TestInfo testInfo){
        return testInfo.getTestMethod().orElse(null)
                .getName().equals("testCreateCommentForPost") ||
                testInfo.getTestMethod().orElse(null)
                .getName().equals("testUpdateCommentForPost")||
                testInfo.getTestMethod().orElse(null)
                .getName().equals("testDeleteCommentForPost");
    }

    @BeforeEach
    void setUp(TestInfo testInfo) {
        if (testRequiresAuthentication(testInfo)){
            user = User.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .email("john.doe@exmaple.com")
                    .build();

            when(authentication.getName()).thenReturn(user.getEmail());
            when(securityContext.getAuthentication()).thenReturn(authentication);
            mockedStatic = mockStatic(SecurityContextHolder.class);
            when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        }
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        if(testRequiresAuthentication(testInfo)){
            mockedStatic.close();
        }
    }

    @Test
    public void testGetAllCommentsForPost(){
        Comment comment = Comment.builder()
                .body("Yeah, I <3 unit testing")
                .build();

        Post post = Post.builder()
                .id(1L)
                .title("Unit test your code")
                .body("Unit testing is very important")
                .comments(new ArrayList<>(List.of(comment)))
                .build();

        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));

        List<Comment> postComments = commentService.getAllCommentsForPost(post.getId());

        assertThat(postComments.size()).isEqualTo(1);

        assertThat(postComments.get(0)).isEqualTo(comment);
    }

    @Test
    public void testCreateCommentForPost(){
        Comment comment = Comment.builder()
                .body("Yeah, I <3 unit testing")
                .build();

        Post post = Post.builder()
                .id(1L)
                .title("Unit test your code")
                .body("Unit testing is very important")
                .user(user)
                .build();

        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        ArgumentCaptor<Comment> commentArgumentCaptor =
                ArgumentCaptor.forClass(Comment.class);

        commentService.createCommentForPost(post.getId(), comment);

        verify(commentRepository).save(commentArgumentCaptor.capture());

        assertThat(commentArgumentCaptor.getValue().getUser()).isEqualTo(user);
        assertThat(commentArgumentCaptor.getValue().getPost()).isEqualTo(post);
    }

    @Test
    public void testUpdateCommentForPost(){
        Post post = Post.builder()
                .id(1L)
                .title("Unit test your code")
                .body("Unit testing is very important")
                .user(user)
                .build();

        Comment commentBeforeUpdate = Comment.builder()
                .body("Yeah, I <3 unit testing")
                .post(post)
                .user(user)
                .build();

        Comment commentUpdateData = Comment.builder()
                .body("Yeah, I <3 unit testing (updated)")
                .build();


        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(commentBeforeUpdate.getId()))
                .thenReturn(Optional.of(commentBeforeUpdate));

        ArgumentCaptor<Comment> commentArgumentCaptor =
                ArgumentCaptor.forClass(Comment.class);

        commentService.updateCommentForPost(
                post.getId(), commentBeforeUpdate.getId(), commentUpdateData);

        verify(commentRepository).save(commentArgumentCaptor.capture());

        assertThat(commentArgumentCaptor.getValue().getBody())
                .isEqualTo(commentUpdateData.getBody());
    }

    @Test
    public void testDeleteCommentForPost(){
        Post post = Post.builder()
                .id(1L)
                .title("Unit test your code")
                .body("Unit testing is very important")
                .user(user)
                .build();

        Comment comment = Comment.builder()
                .body("Yeah, I <3 unit testing")
                .post(post)
                .user(user)
                .build();

        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));

        when(commentRepository.findById(comment.getId()))
                .thenReturn(Optional.of(comment));

        ArgumentCaptor<Comment> commentArgumentCaptor =
                ArgumentCaptor.forClass(Comment.class);

        commentService.deleteCommentForPost(
                post.getId(), comment.getId());

        verify(commentRepository).delete(commentArgumentCaptor.capture());

        assertThat(commentArgumentCaptor.getValue()).isEqualTo(comment);
    }
}