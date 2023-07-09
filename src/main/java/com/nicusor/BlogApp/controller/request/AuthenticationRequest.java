package com.nicusor.BlogApp.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    @Email
    @NotEmpty(message = "Email is mandatory")
    @Size(max = 150)
    private String email;
    @NotEmpty(message = "Password is mandatory")
    @Size(max = 100)
    private String password;
}
