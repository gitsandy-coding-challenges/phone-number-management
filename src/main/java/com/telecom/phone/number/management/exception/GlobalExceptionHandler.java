package com.telecom.phone.number.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Global exception handler for handling exceptions throughout the application.
 *
 * <p>This class is annotated with {@link ControllerAdvice}, which allows it to handle exceptions
 * globally for all controllers. Specific exception handling methods are provided for various
 * exception types, returning appropriate HTTP status codes and error messages.</p>
 *
 * <p>The following exceptions are handled:
 * <ul>
 *   <li>{@link NoHandlerFoundException} - For requests to nonexistent endpoints.</li>
 *   <li>{@link ResourceNotFoundException} - For cases where a requested resource is not found.</li>
 *   <li>{@link NumberAlreadyActivatedException} - For cases where an already active number is activated again.</li>
 *   <li>{@link Exception} - A generic handler for all other unexpected exceptions.</li>
 * </ul>
 * </p>
 *
 * @author Sandeep
 * @version 1.0
 * @since 2025-01-27
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The requested URL " + ex.getRequestURL() + " was not found on this server.");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NumberAlreadyActivatedException.class)
    public ResponseEntity<String> handleNumberAlreadyActivatedException(NumberAlreadyActivatedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}