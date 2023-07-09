package com.nicusor.BlogApp.exception;

public class UserEmailTakenException extends RuntimeException{
    public UserEmailTakenException(String message) {
        super(message);
    }
}
