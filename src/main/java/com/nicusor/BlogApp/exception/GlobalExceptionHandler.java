package com.nicusor.BlogApp.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errorMessages = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
            errorMessages.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
    }

    @ExceptionHandler({
            UserEmailTakenException.class,
            UsernameNotFoundException.class,
            PostTitleExists.class,
            PostDoesNotBelongToUser.class,
            PostNotFoundException.class,
            CommentNotFoundException.class,
            CommentDoesNotBelongToPostException.class,
            CommentDoesNotBelongToUser.class})
    public ResponseEntity<String> handleUniqueFieldException(
            RuntimeException ex
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


}
