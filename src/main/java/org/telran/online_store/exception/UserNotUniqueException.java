package org.telran.online_store.exception;

public class UserNotUniqueException extends RuntimeException{
    public UserNotUniqueException(String message) {
        super(message);
    }
}
