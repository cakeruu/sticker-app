package com.grup30.stickerapp.config;

import com.grup30.stickerapp.application.dto.ErrorResponse;
import com.grup30.stickerapp.application.exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Environment environment;

    public GlobalExceptionHandler(Environment environment) {
        this.environment = environment;
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return handleException(e, e.getStatusCode());
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        System.out.println("Request URI: " + request.getRequestURI());
        return handleException(e, handleDataIntegrityViolationExceptionMessage(request), HttpStatus.BAD_REQUEST);
    }

    private String handleDataIntegrityViolationExceptionMessage(HttpServletRequest request) {
        if (request.getRequestURI().contains("auth/members")) return "Email already in use";
        return "Inputs repeated on our database";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return handleException(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> handleException(Exception e, HttpStatus statusCode) {
        printError(e);
        return ResponseEntity
                .status(statusCode)
                .body(new ErrorResponse(e.getMessage(), statusCode.value()));
    }
    private ResponseEntity<ErrorResponse> handleException(Exception e, String message, HttpStatus statusCode) {
        printError(e);
        return ResponseEntity
                .status(statusCode)
                .body(new ErrorResponse(message, statusCode.value()));
    }


    private void printError(Exception e) {
        if (environment.acceptsProfiles(Profiles.of("dev"))) {
            System.err.println(e.getClass().getSimpleName()+ ": "
                    + e.getMessage()+ "\n"
                    + Arrays.toString(e.getStackTrace())
                    .replace("[", "")
                    .replace("]", "\n")
                    .replace(",", "\n"));
        }
    }
}
