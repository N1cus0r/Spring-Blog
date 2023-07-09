package com.nicusor.BlogApp.service;

import com.nicusor.BlogApp.model.Post;
import com.nicusor.BlogApp.model.User;
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
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    private MockedStatic<SecurityContextHolder> mockedStatic;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private PostService postService;
    private User user;

    private boolean testRequiresAuthentication(TestInfo testInfo){
        return testInfo.getTestMethod().orElse(null)
                .getName().equals("testCreatePost") ||
                testInfo.getTestMethod().orElse(null)
                        .getName().equals("testUpdatePost") ||
                testInfo.getTestMethod().orElse(null)
                        .getName().equals("testDeletePost");
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
    @BeforeTestMethod()
    public void testGetAllPosts(){
        postService.getAllPosts();
        verify(postRepository).findAll();
    }

    @Test
    public void testGetPostDetails(){
        Post post = Post.builder()
                .id(1L)
                .title("Unit test your code")
                .body("Unit testing is very important")
                .user(user)
                .build();

        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));

        postService.getPostDetails(1L);

        ArgumentCaptor<Long> postIdArgumentCaptor =
                ArgumentCaptor.forClass(Long.class);

        verify(postRepository).findById(postIdArgumentCaptor.capture());

        assertThat(postIdArgumentCaptor.getValue()).isEqualTo(post.getId());
    }

    @Test
    public void testCreatePost(){
        Post post = Post.builder()
                .title("Unit test your code")
                .body("Unit testing is very important")
                .build();

        when(SecurityContextHolder.getContext().getAuthentication().getName())
                .thenReturn(user.getEmail());

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        postService.create(post);

        ArgumentCaptor<Post> postArgumentCaptor =
                ArgumentCaptor.forClass(Post.class);

        verify(postRepository).save(postArgumentCaptor.capture());

        assertThat(postArgumentCaptor.getValue()).isEqualTo(post);
    }

    @Test
    public void testUpdatePost(){
        Post postBeforeUpdate = Post.builder()
                .id(1L)
                .title("Unit test your code")
                .body("Unit testing is very important")
                .user(user)
                .build();

        Post postUpdateData = Post.builder()
                .id(1L)
                .title("Unit test your code (Updated)")
                .body("Unit testing is very important (Updated)")
                .user(user)
                .build();

        when(postRepository.findById(postBeforeUpdate.getId()))
                .thenReturn(Optional.of(postBeforeUpdate));

        postService.update(postBeforeUpdate.getId(), postUpdateData);

        ArgumentCaptor<Post> postArgumentCaptor =
                ArgumentCaptor.forClass(Post.class);

        verify(postRepository).save(postArgumentCaptor.capture());

        assertThat(postArgumentCaptor.getValue()).isEqualTo(postUpdateData);
    }

    @Test
    public void testDeletePost(){
        Post post = Post.builder()
                .id(1L)
                .title("Unit test your code")
                .body("Unit testing is very important")
                .user(user)
                .build();

        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.of(post));

        postService.delete(post.getId());

        ArgumentCaptor<Post> postArgumentCaptor =
                ArgumentCaptor.forClass(Post.class);

        verify(postRepository).delete(postArgumentCaptor.capture());

        assertThat(postArgumentCaptor.getValue()).isEqualTo(post);
    }
}