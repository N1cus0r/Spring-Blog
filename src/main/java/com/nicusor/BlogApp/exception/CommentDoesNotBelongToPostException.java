package com.nicusor.BlogApp.exception;

public class CommentDoesNotBelongToPostException extends RuntimeException {
    public CommentDoesNotBelongToPostException(String message) {
        super(message);
    }
}
