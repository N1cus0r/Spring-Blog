package com.nicusor.BlogApp.exception;

public class CommentDoesNotBelongToUser extends RuntimeException {
    public CommentDoesNotBelongToUser(String message) {
        super(message);
    }
}
