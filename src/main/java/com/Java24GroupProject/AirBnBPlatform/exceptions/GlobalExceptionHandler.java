package com.Java24GroupProject.AirBnBPlatform.exceptions;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.DispatcherServlet;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final HttpServletResponse httpServletResponse;
    private final DispatcherServlet dispatcherServlet;
    private final DispatcherServletAutoConfiguration dispatcherServletAutoConfiguration;

    public GlobalExceptionHandler(HttpServletResponse httpServletResponse, DispatcherServlet dispatcherServlet, DispatcherServletAutoConfiguration dispatcherServletAutoConfiguration) {
        this.httpServletResponse = httpServletResponse;
        this.dispatcherServlet = dispatcherServlet;
        this.dispatcherServletAutoConfiguration = dispatcherServletAutoConfiguration;
    }

    // IllegalArgumentException returns 400 bad request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<String> unsupportedOperationExceptionHandler(UnsupportedOperationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> unauthorizedExceptionHandler(UnauthorizedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> authenticationDeniedExceptionHandler(AuthorizationDeniedException ex) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NameAlreadyBoundException.class)
    public ResponseEntity<String> conflictExceptionHandler(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    //error handling for @RequestBody failing @Valid check
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> validationExceptionHandler(MethodArgumentNotValidException ex) {
        String errorMessages = "Invalid input argument(s):";

        //extract the part of the error message that correlates with the "message" string set in the annotation in the class
        for (ObjectError objectError : ex.getAllErrors()) {
            String[] errorFields = objectError.toString().split(";");
            String errorMessage = errorFields[errorFields.length-1].substring(18).replace("]","");
            //if error message is not a duplicate of an existing error message, add to String
            if (!errorMessages.contains(errorMessage)) {
                errorMessages = errorMessages.concat("\n- " + errorMessage);
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> generalExceptionHandler(Exception ex) {
        return new ResponseEntity<>("Unexpected error:\n"+ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
