package com.example.canvasintegration.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e, WebRequest request) {
        logger.error("Unhandled exception occurred: ", e);

        ErrorResponse errorResponse = new ErrorResponse(
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                request.getDescription(false)
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    // Handle HTTP client errors
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException e, WebRequest request) {
        logger.error("Canvas API error: ", e);

        ErrorResponse errorResponse = new ErrorResponse(
                "Canvas API Error",
                String.format("Status: %s, Details: %s", e.getStatusCode(), e.getResponseBodyAsString()),
                request.getDescription(false)
        );

        return ResponseEntity
                .status(e.getStatusCode())
                .body(errorResponse);
    }

    // Handle illegal arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
        logger.error("Illegal argument: ", e);

        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid Request",
                e.getMessage(),
                request.getDescription(false)
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    // Handle null pointer exceptions
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e, WebRequest request) {
        logger.error("Null pointer exception: ", e);

        ErrorResponse errorResponse = new ErrorResponse(
                "Null Pointer Exception",
                "A required object was missing. Please contact support.",
                request.getDescription(false)
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
