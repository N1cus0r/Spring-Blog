package com.nicusor.BlogApp.exception;

public class PostDoesNotBelongToUser extends RuntimeException{
    public PostDoesNotBelongToUser(String message) {
        super(message);
    }
}
