package org.vitaliistf.userapi.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.vitaliistf.userapi.exception.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for handling and processing various types of exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String DETAILS_FIELD = "details";
    private static final String ERROR_CODE_FIELD = "errorCode";
    private static final String TIMESTAMP_FIELD = "timestamp";

    /**
     * Handles ResourceNotFoundException and returns a corresponding error response.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Handles InvalidAgeException and returns a corresponding error response.
     */
    @ExceptionHandler(InvalidAgeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidAgeException(InvalidAgeException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles InvalidDateRangeException and returns a corresponding error response.
     */
    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidDateRangeException(InvalidDateRangeException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Handles ConstraintViolationException and returns a corresponding error response.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> violationDetails = getConstraintViolationDetails(e);
        return buildErrorResponseBody(HttpStatus.BAD_REQUEST, violationDetails);
    }

    /**
     * Handles TypeMismatchException and returns a corresponding error response.
     */
    @ExceptionHandler(TypeMismatchException.class)
    protected ResponseEntity<Object> handleTypeMismatchException(TypeMismatchException e) {
        return buildErrorResponseBody(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * Handles EmailAlreadyExistsException and returns a corresponding error response.
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    protected ResponseEntity<Object> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        return buildErrorResponseBody(HttpStatus.CONFLICT, e.getMessage());
    }

    /**
     * Handles PhoneNumberAlreadyExistsException and returns a corresponding error response.
     */
    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    protected ResponseEntity<Object> handlePhoneNumberAlreadyExistsException(PhoneNumberAlreadyExistsException e) {
        return buildErrorResponseBody(HttpStatus.CONFLICT, e.getMessage());
    }

    /**
     * Handles MethodArgumentNotValidException and returns a corresponding error response.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> validationDetails = getMethodArgumentValidationDetails(e);
        return buildErrorResponseBody(HttpStatus.BAD_REQUEST, validationDetails);
    }

    /**
     * Extracts details of method argument validation from MethodArgumentNotValidException.
     */
    private Map<String, String> getMethodArgumentValidationDetails(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage));
    }

    /**
     * Extracts details of ConstraintViolation from ConstraintViolationException.
     */
    private Map<String, String> getConstraintViolationDetails(ConstraintViolationException e) {
        return e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> String.valueOf(violation.getPropertyPath()),
                        ConstraintViolation::getMessage));
    }

    /**
     * Builds the error response with a single string message.
     */
    private ResponseEntity<Map<String, String>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }

    /**
     * Builds the error response body with timestamp, error code, and details.
     */
    private ResponseEntity<Object> buildErrorResponseBody(HttpStatus status, Object details) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP_FIELD, LocalDateTime.now());
        body.put(ERROR_CODE_FIELD, status.value());
        body.put(DETAILS_FIELD, details);
        return ResponseEntity.status(status).body(body);
    }
}