package org.telran.online_store.handler;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.telran.online_store.exception.*;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ProductNotFoundException.class, UserNotFoundException.class
            , CategoryNotFoundException.class, FavoriteNotFoundException.class, OrderNotFoundException.class,
            CartNotFoundException.class, CartItemNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<NotFoundErrorResponse> handleEntityNotFoundException(Exception e) {
        NotFoundErrorResponse response = NotFoundErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({FavoriteNotUniqueException.class, UserNotUniqueException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<NotUniqueErrorResponse> handleEntityNotUniqueException(Exception e) {
        NotUniqueErrorResponse response = NotUniqueErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        List<String> errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        ValidationErrorResponse response = ValidationErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errors(errorMessages)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Builder
    public record ValidationErrorResponse(
            @Schema(example = "400")
            int status,

            @Schema(example = "Incorrect data")
            List<String> errors) {
    }

    @Builder
    public record NotUniqueErrorResponse(
            @Schema(example = "409")
            int status,

            @Schema(example = "Already exists")
            String message) {
    }

    @Builder
    public record NotFoundErrorResponse(
            @Schema(example = "404")
            int status,

            @Schema(example = "Not found")
            String message) {
    }

    @Builder
    public record UnauthorizedErrorResponse(
            @Schema(example = "401")
            int status,

            @Schema(example = "Unauthorized: You must be logged in")
            String message
    ) {}
}
