package org.telran.online_store.exception;

public class CartItemNotFoundException extends RuntimeException{

    public CartItemNotFoundException(String message) {

        super(message);
    }
}
