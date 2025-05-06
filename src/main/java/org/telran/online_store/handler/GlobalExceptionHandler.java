package org.telran.online_store.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.telran.online_store.dto.ApiErrorResponse;
import org.telran.online_store.exception.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ProductNotFoundException.class, UserNotFoundException.class
            , CategoryNotFoundException.class, FavoriteNotFoundException.class, OrderNotFoundException.class,
            CartNotFoundException.class, CartItemNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFoundException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({FavoriteNotUniqueException.class, UserNotUniqueException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiErrorResponse> handleEntityNotUniqueException(Exception e, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse();
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setMessage(e.getMessage());
        response.setPath(request.getRequestURI());
        response.setTimestamp(LocalDateTime.now().toString());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
