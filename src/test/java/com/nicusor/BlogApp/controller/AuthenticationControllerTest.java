package com.nicusor.BlogApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicusor.BlogApp.controller.request.AuthenticationRequest;
import com.nicusor.BlogApp.controller.request.RegisterRequest;
import com.nicusor.BlogApp.controller.response.AuthenticationResponse;
import com.nicusor.BlogApp.service.AuthenticationService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationService authService;
    @MockBean
    private JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegister() throws Exception{
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();

        AuthenticationResponse tokenResponse = AuthenticationResponse.builder()
                .token("Bearer token")
                .build();

        when(authService.register(ArgumentMatchers.any()))
                .thenReturn(tokenResponse);

        ResultActions response = mockMvc
                .perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.token", CoreMatchers.is(tokenResponse.getToken())));
    }

    @Test
    public void testAuthenticate() throws Exception{
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("john.doe@example.com")
                .password("password")
                .build();

        AuthenticationResponse tokenResponse = AuthenticationResponse.builder()
                .token("Bearer token")
                .build();

        when(authService.authenticate(ArgumentMatchers.any()))
                .thenReturn(tokenResponse);


        ResultActions response = mockMvc
                .perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath(
                        "$.token", CoreMatchers.is(tokenResponse.getToken())));

    }

}