package com.nicusor.BlogApp.service;

import com.nicusor.BlogApp.controller.request.AuthenticationRequest;
import com.nicusor.BlogApp.controller.request.RegisterRequest;
import com.nicusor.BlogApp.model.Role;
import com.nicusor.BlogApp.model.User;
import com.nicusor.BlogApp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    public void testRegister(){
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@exmaple.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.USER)
                .build();

        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@exmaple.com")
                .password("password")
                .build();

        ArgumentCaptor<User> userCreataionArgumentCaptor =
                ArgumentCaptor.forClass(User.class);

        ArgumentCaptor<User> jwtTokenGenerationArgumentCaptor =
                ArgumentCaptor.forClass(User.class);

        authenticationService.register(request);

        verify(userRepository).save(userCreataionArgumentCaptor.capture());

        verify(jwtService).generateToken(jwtTokenGenerationArgumentCaptor.capture());

        assertThat(userCreataionArgumentCaptor.getValue()).isEqualTo(user);

        assertThat(jwtTokenGenerationArgumentCaptor.getValue()).isEqualTo(user);
    }

    @Test
    public void testAuthenticate(){
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@exmaple.com")
                .password(passwordEncoder.encode("password"))
                .role(Role.USER)
                .build();

        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("john.doe@exmaple.com")
                .password("password")
                .build();

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword());

        ArgumentCaptor<UsernamePasswordAuthenticationToken> authTokenArgumentCaptor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);

        ArgumentCaptor<User> jwtTokenGenerationArgumentCaptor =
                ArgumentCaptor.forClass(User.class);

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        authenticationService.authenticate(request);

        verify(authenticationManager).authenticate(authTokenArgumentCaptor.capture());

        verify(jwtService).generateToken(jwtTokenGenerationArgumentCaptor.capture());

        assertThat(authTokenArgumentCaptor.getValue()).isEqualTo(authToken);

        assertThat(jwtTokenGenerationArgumentCaptor.getValue()).isEqualTo(user);
    }
}