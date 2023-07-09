package com.nicusor.BlogApp.exception;
public class PostTitleExists extends RuntimeException {
    public PostTitleExists(String message) {
        super(message);
    }
}
