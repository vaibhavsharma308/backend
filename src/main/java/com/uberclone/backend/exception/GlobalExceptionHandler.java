package com.uberclone.backend.exception;


import com.uberclone.backend.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setMessage(ex.getMessage());
        LocalDateTime timestamp = LocalDateTime.now();
        errorResponse.setTimestamp(timestamp);
        errorResponse.setDetail("Error Occurred");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodNotSupportedException( MethodArgumentNotValidException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setMessage(ex.getMessage());
        LocalDateTime timestamp = LocalDateTime.now();
        errorResponse.setTimestamp(timestamp);
        errorResponse.setDetail("Error Occurred");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(
            IllegalArgumentException ex) {

        ErrorResponseDTO error = new ErrorResponseDTO();
        error.setMessage(ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setDetail("Bad request");

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
